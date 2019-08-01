/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.optDRT;

import org.matsim.core.config.ReflectiveConfigGroup;

/**
 * 
 * @author ikaddoura
 */

public class OptDrtConfigGroup extends ReflectiveConfigGroup {
	public static final String GROUP_NAME = "optDrt" ;

	private static final String OPT_DRT_MODE = "optDrtMode";

	private static final String UPDATE_INTERVAL = "updateInterval";
	private static final String UPDATE_END_FRACTION_ITERATION = "updateEndFractionIteration";
	private static final String WRITE_INFO_INTERVAL = "writeInfoInterval";

	private static final String FARE_ADJUSTMENT = "fareAdjustment";
	private static final String FARE_ADJUSTMENT_APPROACH = "fareAdjustmentApproach";
	private static final String WAITING_TIME_THRESHOLD_FOR_FARE_ADJUSTMENT = "waitingTimeThresholdForFareAdjustment";
	private static final String FARE_TIME_BIN_SIZE = "fareTimeBinSize";

	private static final String FLEETSIZE_ADJUSTMENT = "fleetSizeAdjustment";
	private static final String FLEETSIZE_ADJUSTMENT_APPROACH = "fleetSizeAdjustmentApproach";
	private static final String PROFIT_THRESHOLD_FOR_FLEETSIZE_ADJUSTMENT = "profitThresholdForFleetSizeAdjustment";
	private static final String COST_PER_VEHICLE_PER_DAY_FOR_FLEET_ADJUSTMENT = "costPerVehPerDayForFleetSizeAdjustment";
	private static final String COST_PER_VEHICLE_PER_METER_FOR_FLEET_ADJUSTMENT = "costPerVehPerMeterForFleetSizeAdjustment";
	private static final String WAITING_TIME_THRESHOLD_FOR_FLEET_ADJUSTMENT = "waitingTimeThresholdForFleetSizeAdjustment";
	private static final String FlEETSIZE_TIME_BIN_SIZE = "fleetSizeTimeBinSize";

	private static final String SERVICE_AREA_ADJUSTMENT = "serviceAreaAdjustment";
	private static final String SERVICE_AREA_ADJUSTMENT_APPROACH = "serviceAreaAdjustmentApproach";
	private static final String INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT = "inputShapeFileForServiceAreaAdjustment";
	private static final String INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT_CRS = "inputShapeFileForServiceAreaAdjustmentCRS";
	private static final String INPUT_SHAPEFILE_INITIAL_SERVICE_AREA = "inputShapeFileInitialServiceArea";
	private static final String INPUT_SHAPEFILE_INITIAL_SERVICE_AREA_CRS = "inputShapeFileInitialServiceAreaCRS";
	private static final String DEMAND_THRESHOLD_FOR_SERVICE_AREA_ADJUSTMENT = "demandThresholdForServiceAreaAdjustment";

	public OptDrtConfigGroup() {
		super(GROUP_NAME);
	}
	
	private String optDrtMode = "drt";
	private int updateInterval = 1;
	private double updateEndFractionIteration = 0.8;
	private int writeInfoInterval = 1;

	private FareAdjustmentApproach fareAdjustmentApproach = FareAdjustmentApproach.AverageWaitingTimeThreshold;
	private double fareAdjustment = 0.5;
	private double fareTimeBinSize = 900.;
	private double waitingTimeThresholdForFareAdjustment = 600.;

	private FleetSizeAdjustmentApproach fleetSizeAdjustmentApproach = FleetSizeAdjustmentApproach.AverageWaitingTimeThreshold;
	private int fleetSizeAdjustment = 1;
	// profit approach
	private double profitThresholdForFleetSizeAdjustment = 0.;
	private double costPerVehPerDayForFleetAdjustment = 5.3;
	private double costPerVehPerMeterForFleetAdjustment = 0.35 / 1000.;
	// waiting time approach
	private double fleetSizeTimeBinSize = 900.;
	private double waitingTimeThresholdForFleetSizeAdjustment = 600.;

	private ServiceAreaAdjustmentApproach serviceAreaAdjustmentApproach = ServiceAreaAdjustmentApproach.Disabled;
	private String inputShapeFileForServiceAreaAdjustment = null;
	private String inputShapeFileForServiceAreaAdjustmentCrs = null;
	private String inputShapeFileInitialServiceArea = null;
	private String inputShapeFileInitialServiceAreaCrs = null;
	private int serviceAreaAdjustment = 1;
	private int demandThresholdForServiceAreaAdjustment = 1;

	public enum FareAdjustmentApproach {
		Disabled, AverageWaitingTimeThreshold
	}

	public enum ServiceAreaAdjustmentApproach {
		Disabled, DemandThreshold
	}

	public enum FleetSizeAdjustmentApproach {
		Disabled, ProfitThreshold, AverageWaitingTimeThreshold
	}

	@StringGetter( DEMAND_THRESHOLD_FOR_SERVICE_AREA_ADJUSTMENT )
	public int getDemandThresholdForServiceAreaAdjustment() {
		return demandThresholdForServiceAreaAdjustment;
	}

	@StringSetter( DEMAND_THRESHOLD_FOR_SERVICE_AREA_ADJUSTMENT )
	public void setDemandThresholdForServiceAreaAdjustment(int demandThresholdForServiceAreaAdjustment) {
		this.demandThresholdForServiceAreaAdjustment = demandThresholdForServiceAreaAdjustment;
	}

	@StringGetter( SERVICE_AREA_ADJUSTMENT )
	public int getServiceAreaAdjustment() {
		return serviceAreaAdjustment;
	}

	@StringSetter( SERVICE_AREA_ADJUSTMENT )
	public void setServiceAreaAdjustment(int serviceAreaAdjustment) {
		this.serviceAreaAdjustment = serviceAreaAdjustment;
	}
	
	@StringGetter( OPT_DRT_MODE )
	public String getOptDrtMode() {
		return optDrtMode;
	}

	@StringSetter( OPT_DRT_MODE )
	public void setOptDrtMode(String optDrtMode) {
		this.optDrtMode = optDrtMode;
	}

	@StringGetter( FARE_ADJUSTMENT )
	public double getFareAdjustment() {
		return fareAdjustment;
	}

	@StringSetter( FARE_ADJUSTMENT )
	public void setFareAdjustment(double fareAdjustment) {
		this.fareAdjustment = fareAdjustment;
	}

	@StringGetter( UPDATE_INTERVAL )
	public int getUpdateInterval() {
		return updateInterval;
	}
	
	@StringSetter( UPDATE_INTERVAL )
	public void setUpdateInterval(int uPDATE_INTERVAL) {
		updateInterval = uPDATE_INTERVAL;
	}

	@StringGetter( UPDATE_END_FRACTION_ITERATION )
	public double getUpdateEndFractionIteration() {
		return updateEndFractionIteration;
	}

	@StringSetter( UPDATE_END_FRACTION_ITERATION )
	public void setUpdateEndIterationFraction(double updateEndFractionIteration) {
		this.updateEndFractionIteration = updateEndFractionIteration;
	}

	@StringGetter( FARE_TIME_BIN_SIZE )
	public double getFareTimeBinSize() {
		return fareTimeBinSize;
	}

	@StringSetter( FARE_TIME_BIN_SIZE )
	public void setFareTimeBinSize(double fareTimeBinSize) {
		this.fareTimeBinSize = fareTimeBinSize;
	}

	@StringGetter( WAITING_TIME_THRESHOLD_FOR_FARE_ADJUSTMENT )
	public double getWaitingTimeThresholdForFareAdjustment() {
		return waitingTimeThresholdForFareAdjustment;
	}

	@StringSetter( WAITING_TIME_THRESHOLD_FOR_FARE_ADJUSTMENT )
	public void setWaitingTimeThresholdForFareAdjustment(double waitingTimeThresholdForFareAdjustment) {
		this.waitingTimeThresholdForFareAdjustment = waitingTimeThresholdForFareAdjustment;
	}

	@StringGetter( FLEETSIZE_ADJUSTMENT )
	public int getFleetSizeAdjustment() {
		return fleetSizeAdjustment;
	}

	@StringSetter( FLEETSIZE_ADJUSTMENT )
	public void setFleetSizeAdjustment(int fleetSizeAdjustment) {
		this.fleetSizeAdjustment = fleetSizeAdjustment;
	}

	@StringGetter( PROFIT_THRESHOLD_FOR_FLEETSIZE_ADJUSTMENT )
	public double getProfitThresholdForFleetSizeAdjustment() {
		return profitThresholdForFleetSizeAdjustment;
	}

	@StringSetter( PROFIT_THRESHOLD_FOR_FLEETSIZE_ADJUSTMENT )
	public void setProfitThresholdForFleetSizeAdjustment(double profitThresholdForFleetSizeAdjustment) {
		this.profitThresholdForFleetSizeAdjustment = profitThresholdForFleetSizeAdjustment;
	}

	@StringGetter( COST_PER_VEHICLE_PER_DAY_FOR_FLEET_ADJUSTMENT )
	public double getCostPerVehPerDayForFleetAdjustment() {
		return costPerVehPerDayForFleetAdjustment;
	}

	@StringSetter( COST_PER_VEHICLE_PER_DAY_FOR_FLEET_ADJUSTMENT )
	public void setCostPerVehPerDayForFleetAdjustment(double costPerVehPerDayForFleetAdjustment) {
		this.costPerVehPerDayForFleetAdjustment = costPerVehPerDayForFleetAdjustment;
	}

	@StringGetter( COST_PER_VEHICLE_PER_METER_FOR_FLEET_ADJUSTMENT )
	public double getCostPerVehPerMeterForFleetAdjustment() {
		return costPerVehPerMeterForFleetAdjustment;
	}

	@StringSetter( COST_PER_VEHICLE_PER_METER_FOR_FLEET_ADJUSTMENT )
	public void setCostPerVehPerMeterForFleetAdjustment(double costPerVehPerMeterForFleetAdjustment) {
		this.costPerVehPerMeterForFleetAdjustment = costPerVehPerMeterForFleetAdjustment;
	}

	@StringGetter( INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT )
	public String getInputShapeFileForServiceAreaAdjustment() {
		return inputShapeFileForServiceAreaAdjustment;
	}

	@StringSetter( INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT )
	public void setInputShapeFileForServiceAreaAdjustment(String inputShapeFileForServiceAreaAdjustment) {
		this.inputShapeFileForServiceAreaAdjustment = inputShapeFileForServiceAreaAdjustment;
	}

	@StringGetter( INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT_CRS )
	public String getInputShapeFileForServiceAreaAdjustmentCrs() {
		return inputShapeFileForServiceAreaAdjustmentCrs;
	}

	@StringSetter( INPUT_SHAPEFILE_FOR_SERVICE_AREA_ADJUSTMENT_CRS )
	public void setInputShapeFileForServiceAreaAdjustmentCrs(String inputShapeFileForServiceAreaAdjustmentCrs) {
		this.inputShapeFileForServiceAreaAdjustmentCrs = inputShapeFileForServiceAreaAdjustmentCrs;
	}
	
	@StringGetter( INPUT_SHAPEFILE_INITIAL_SERVICE_AREA )
	public String getInputShapeFileInitialServiceArea() {
		return inputShapeFileInitialServiceArea;
	}

	@StringSetter( INPUT_SHAPEFILE_INITIAL_SERVICE_AREA )
	public void setInputShapeFileInitialServiceArea(String inputShapeFileInitialServiceArea) {
		this.inputShapeFileInitialServiceArea = inputShapeFileInitialServiceArea;
	}

	@StringGetter( INPUT_SHAPEFILE_INITIAL_SERVICE_AREA_CRS )
	public String getInputShapeFileInitialServiceAreaCrs() {
		return inputShapeFileInitialServiceAreaCrs;
	}

	@StringSetter( INPUT_SHAPEFILE_INITIAL_SERVICE_AREA_CRS )
	public void setInputShapeFileInitialServiceAreaCrs(String inputShapeFileInitialServiceAreaCrs) {
		this.inputShapeFileInitialServiceAreaCrs = inputShapeFileInitialServiceAreaCrs;
	}

	@StringGetter( FARE_ADJUSTMENT_APPROACH )
	public FareAdjustmentApproach getFareAdjustmentApproach() {
		return fareAdjustmentApproach;
	}

	@StringSetter( FARE_ADJUSTMENT_APPROACH )
	public void setFareAdjustmentApproach(FareAdjustmentApproach fareAdjustmentApproach) {
		this.fareAdjustmentApproach = fareAdjustmentApproach;
	}

	@StringGetter( FLEETSIZE_ADJUSTMENT_APPROACH )
	public FleetSizeAdjustmentApproach getFleetSizeAdjustmentApproach() {
		return fleetSizeAdjustmentApproach;
	}

	@StringSetter( FLEETSIZE_ADJUSTMENT_APPROACH )
	public void setFleetSizeAdjustmentApproach(FleetSizeAdjustmentApproach fleetSizeAdjustmentApproach) {
		this.fleetSizeAdjustmentApproach = fleetSizeAdjustmentApproach;
	}

	@StringGetter( WAITING_TIME_THRESHOLD_FOR_FLEET_ADJUSTMENT )
	public double getWaitingTimeThresholdForFleetSizeAdjustment() {
		return waitingTimeThresholdForFleetSizeAdjustment;
	}

	@StringSetter( WAITING_TIME_THRESHOLD_FOR_FLEET_ADJUSTMENT )
	public void setWaitingTimeThresholdForFleetSizeAdjustment(double waitingTimeThresholdForFleetSizeAdjustment) {
		this.waitingTimeThresholdForFleetSizeAdjustment = waitingTimeThresholdForFleetSizeAdjustment;
	}

	@StringGetter( SERVICE_AREA_ADJUSTMENT_APPROACH )
	public ServiceAreaAdjustmentApproach getServiceAreaAdjustmentApproach() {
		return serviceAreaAdjustmentApproach;
	}

	@StringSetter( SERVICE_AREA_ADJUSTMENT_APPROACH )
	public void setServiceAreaAdjustmentApproach(ServiceAreaAdjustmentApproach serviceAreaAdjustmentApproach) {
		this.serviceAreaAdjustmentApproach = serviceAreaAdjustmentApproach;
	}

	@StringGetter( FlEETSIZE_TIME_BIN_SIZE )
	public double getFleetSizeTimeBinSize() {
		return fleetSizeTimeBinSize;
	}

	@StringSetter( FlEETSIZE_TIME_BIN_SIZE )
	public void setFleetSizeTimeBinSize(double fleetSizeTimeBinSize) {
		this.fleetSizeTimeBinSize = fleetSizeTimeBinSize;
	}

	@StringGetter( WRITE_INFO_INTERVAL )
	public int getWriteInfoInterval() {
		return writeInfoInterval;
	}

	@StringSetter( WRITE_INFO_INTERVAL )
	public void setWriteInfoInterval(int writeInfoInterval) {
		this.writeInfoInterval = writeInfoInterval;
	}

	
			
}
