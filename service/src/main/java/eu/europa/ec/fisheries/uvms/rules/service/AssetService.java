package eu.europa.ec.fisheries.uvms.rules.service;

import org.joda.time.DateTime;

import javax.ejb.Local;

@Local
public interface AssetService {

    boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime date);
}
