package org.matsim.optDRT;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.PersonMoneyEvent;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFareConfigGroup;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEventHandler;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.optDRT.analysis.DrtModeStatsControlerListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zmeng
 *
 * An implementation for different DRT fares for different times of day.
 * The fares will be updated during the simulation depending on modestats.
 *
 * Note that these fares are scored in excess to anything set in the modeparams in the config file or any other drt fare handler.
 */
public class OptDrtFareStrategyModalSplit implements PersonDepartureEventHandler, PersonArrivalEventHandler, OptDrtFareStrategy, DrtRequestSubmittedEventHandler {
    private static final Logger log = Logger.getLogger(OptDrtFareStrategyWaitingTime.class);

    private Map<Integer,Double> timeBin2distanceFarePerMeter = new HashMap<>();

    private Map<Id<Person>, DrtRequestSubmittedEvent> lastRequestSubmission = new HashMap<>();
    private Map<Id<Person>, Double> drtUserDepartureTime = new HashMap<>();
    private Map<Integer, Double> timeBin2DrtModalStats = new HashMap<>();

    private int currentIteration;

    @Inject
    OptDrtConfigGroup optDrtConfigGroup;

    @Inject
    EventsManager events;

    @Inject
    private Scenario scenario;

    @Inject
    DrtModeStatsControlerListener drtModeStatsControlerListener;

    @Inject
    DrtFareConfigGroup drtFareConfigGroup;

    @Override
    public void handleEvent(PersonArrivalEvent event) {

        if (event.getLegMode().equals(optDrtConfigGroup.getOptDrtMode())) {

            DrtRequestSubmittedEvent e = this.lastRequestSubmission.get(event.getPersonId());

            int timeBin = getTimeBin(drtUserDepartureTime.get(event.getPersonId()));
            double timeBinDistanceFare = 0;
            if (this.timeBin2distanceFarePerMeter.get(timeBin) != null) {
                timeBinDistanceFare = this.timeBin2distanceFarePerMeter.get(timeBin);
            }
            // update the price, and make sure the new price will not be lower than the minFare in drtFareConfig.
            double fare = e.getUnsharedRideDistance() * timeBinDistanceFare;
            double oldFare = Math.max(e.getUnsharedRideDistance() * drtFareConfigGroup.getDistanceFare_m(), drtFareConfigGroup.getMinFarePerTrip());
            if(oldFare + fare < drtFareConfigGroup.getMinFarePerTrip()){
                events.processEvent(new PersonMoneyEvent(event.getTime(), event.getPersonId(), ((-drtFareConfigGroup.getMinFarePerTrip()))+oldFare));
            } else {
                events.processEvent(new PersonMoneyEvent(event.getTime(), event.getPersonId(), -fare));
            }

            this.drtUserDepartureTime.remove(event.getPersonId());
            this.lastRequestSubmission.remove(event.getPersonId());
        }
    }

    private int getTimeBin(Double time) {
        int timeBin =(int)(time / optDrtConfigGroup.getFareTimeBinSize());
        return timeBin;
    }

    @Override
    public void reset(int iteration) {

        lastRequestSubmission.clear();
        drtUserDepartureTime.clear();
        timeBin2DrtModalStats.clear();
        this.currentIteration = iteration;
    }

    @Override
    public void handleEvent(DrtRequestSubmittedEvent event) {
        if(optDrtConfigGroup.getOptDrtMode().equals(event.getMode())){
            this.lastRequestSubmission.put(event.getPersonId(),event);
        }
    }

    @Override
    public void updateFares() {

        this.timeBin2DrtModalStats = this.drtModeStatsControlerListener.getIt2TimeBin2drtModeStats().get(this.currentIteration);

            for(int timeBin = 0; timeBin <= getTimeBin(scenario.getConfig().qsim().getEndTime()); timeBin ++) {
                double drtModeStats = timeBin2DrtModalStats.get(timeBin);

                double oldDistanceFare = 0.;
                if (timeBin2distanceFarePerMeter.get(timeBin) != null) {
                    oldDistanceFare = timeBin2distanceFarePerMeter.get(timeBin);
                }

                double updatedDistanceFare = 0.;
                if (drtModeStats > optDrtConfigGroup.getModalSplitThresholdForFareAdjustment()) {
                    updatedDistanceFare = oldDistanceFare + optDrtConfigGroup.getFareAdjustment();
                } else if (drtModeStats < optDrtConfigGroup.getModalSplitThresholdForFareAdjustment() ){
                    updatedDistanceFare = oldDistanceFare - optDrtConfigGroup.getFareAdjustment();
                }

                // negative price should not be allowed.However, considering that the updated price is based on the original price.
                // Lower than the price defined in the drt-fare module should be taken into account.
                if (updatedDistanceFare < (0 - drtFareConfigGroup.getDistanceFare_m())) updatedDistanceFare = 0 - drtFareConfigGroup.getDistanceFare_m();

                log.info("Fare in time bin " + timeBin + " changed from " + oldDistanceFare + " to " + updatedDistanceFare);

                timeBin2distanceFarePerMeter.put(timeBin, updatedDistanceFare);

        }


    }

    @Override
    public void writeInfo() {

            String runOutputDirectory = this.scenario.getConfig().controler().getOutputDirectory();
            if (!runOutputDirectory.endsWith("/")) runOutputDirectory = runOutputDirectory.concat("/");

            int num = currentIteration + 1;

            String fileName = runOutputDirectory + "ITERS/it." + num + "/" + this.scenario.getConfig().controler().getRunId() + "." + num + ".info_" + this.getClass().getName() + ".csv";
            File file = new File(fileName);

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));

                bw.write("time bin;time bin start time [sec];time bin end time [sec];totalTrips ;drtTrips ; drtModeStats ;fare [monetary units / meter]");
                bw.newLine();

                for (Integer timeBin : this.drtModeStatsControlerListener.getIt2TimeBin2drtModeStats().get(currentIteration).keySet()) {

                    double timeBinStart = timeBin * optDrtConfigGroup.getFareTimeBinSize();
                    double timeBinEnd = timeBin * optDrtConfigGroup.getFareTimeBinSize() + optDrtConfigGroup.getFareTimeBinSize();

                    double fare = 0.;
                    if (this.timeBin2distanceFarePerMeter.get(timeBin) != null) fare = this.timeBin2distanceFarePerMeter.get(timeBin);

                    bw.write(String.valueOf(timeBin) + ";" + timeBinStart + ";" + timeBinEnd + ";" + this.drtModeStatsControlerListener.getIt2TimeBin2totalTrips().get(currentIteration).get(timeBin) + ";"
                            + this.drtModeStatsControlerListener.getIt2TimeBin2drtTrips().get(currentIteration).get(timeBin) + ";" +
                            this.drtModeStatsControlerListener.getIt2TimeBin2drtModeStats().get(currentIteration).get(timeBin) +";" + String.valueOf(fare) );
                    bw.newLine();
                }
                log.info("Output written to " + fileName);
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        if(optDrtConfigGroup.getOptDrtMode().equals(event.getLegMode())){
            this.drtUserDepartureTime.put(event.getPersonId(),event.getTime());
        }

    }
}