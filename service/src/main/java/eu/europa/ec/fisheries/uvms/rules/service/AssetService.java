package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;

import org.joda.time.DateTime;

@Local
public interface AssetService {

    boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime date);

}
