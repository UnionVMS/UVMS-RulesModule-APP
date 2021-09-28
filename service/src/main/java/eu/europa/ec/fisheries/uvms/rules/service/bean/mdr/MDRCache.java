/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.gateway.RulesMdrGateway;
import eu.europa.ec.fisheries.uvms.rules.service.business.FormatExpression;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleFromMDR;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.MdrLoadingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.communication.*;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Gregory Rinaldi, Andi Kovi
 */
@Singleton
@Startup
@Slf4j
public class MDRCache {

    @Getter
    private Map<MDRAcronymType, List<ObjectRepresentation>> cache;

    @Getter
    private List<String> allCodeLists;

    @Inject
    private RulesMdrGateway rulesMdrGateway;

    // This variable will store the date that the last entity in mdr (module) was refreshed.
    // @Info : refresh to 'last_success' column of 'mdr_codelist_status' table in mdr schema.
    private Date mdrRefreshDate;

    private Map<String, List<RuleFromMDR>> enrichedBRMessageMap;

    private Map<String, List<FormatExpression>> formatsByIdentifier;

    private Map<String, List<String>> dataFlowContexts;

    private boolean alreadyLoadedOnce = false;

    private Date lastTimeRefreshDateWasRetrieved;

    private static final int MB = 1024 * 1024;


    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
        enrichedBRMessageMap = new ConcurrentHashMap<>();
        formatsByIdentifier = new ConcurrentHashMap<>();
        dataFlowContexts = new ConcurrentHashMap<>();
        allCodeLists = new ArrayList<>();
    }

    /**
     * It fetches the latest refresh date from MDR and checks if the local cache needs to be refreshed.
     */
    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.WRITE)
    public void loadAllMdrCodeLists(boolean isFromReport) {
        try {
            if (!alreadyLoadedOnce) {
                log.info("MDR Cache was never loaded, loading now..");
                populateMdrCacheDateAndCheckIfRefreshDateChanged();
                populateMdrCache();
            } else if (isFromReport && oneMinuteHasPassed() && populateMdrCacheDateAndCheckIfRefreshDateChanged()) { // We fetch MdrCacheDate only once per minute.
                log.info("MDR Cache in MDR was updated.. Going to refresh Rules mdr cahce now...");
                populateMdrCache();
            }
        } catch (MdrLoadingException e) {
            log.error("Exception while trying to loadAllMdrCodeLists...", e);
        }
    }

    /**
     * Chooses which way to load mdr cache (at once/one by one) depending on the free memory available!
     * In this way we avoid an OutOfMemory Exception of the heapSpace. :)
     */
    private void populateMdrCache() {
        long freeMemory = Runtime.getRuntime().freeMemory() / MB;
        log.info("Free Memory : [{}] MB", freeMemory);
        if ((!alreadyLoadedOnce && freeMemory < 800) || freeMemory < 150) {
            populateAllMdrOneByOne();
        } else {
            populateAllMdrOneByOne(); //populateAllMdrAtOnce(); This one sometimes is to heavy for activemq!
        }
        // The CODELIST_STATUS codelist is loaded separately since it cannot take the same flow cause it doesn't extend @MasterDataRegistry @see @MDR.MasterDataRegistryEntityCacheFactory
        populateAllCodeListStatuses();
        overrideBRMessages();
        populateFormatExpressions();
        populateDataFlowContextMappings();
    }

    private boolean oneMinuteHasPassed() {
        return (Math.abs(new Date().getTime() - lastTimeRefreshDateWasRetrieved.getTime()) / 1000) > 59;
    }

    /**
     * It loads all the mdr codelists one by one from the mdr module [GET_MDR_CODE_LIST] Request Type.
     */
    private void populateAllMdrOneByOne() {
        log.info("Loading MDR..");
        for (final MDRAcronymType type : MDRAcronymType.values()) {
            cache.put(type, mdrCodeListByAcronymType(type));
        }
        log.info("{} lists cached", cache.size());
        alreadyLoadedOnce = true;
        log.info("MDR refresh Date {}", mdrRefreshDate);
    }

    /**
     * It loads all the mdr codelists at once from the mdr module [GET_ALL_MDR_CODE_LIST] Request Type.
     */
    private void populateAllMdrAtOnce() throws MdrLoadingException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Loading All MDR...");
        MdrGetAllCodeListsResponse allMdrCodeLists = getAllMdrCodeLists();
        if (allMdrCodeLists != null && CollectionUtils.isNotEmpty(allMdrCodeLists.getCodeLists())) {
            for (SingleCodeListRappresentation codeList : allMdrCodeLists.getCodeLists()) {
                cache.put(MDRAcronymType.valueOf(codeList.getAcronym()), codeList.getDataSets());
            }
        } else {
            log.warn("Mdr Response resulted null/empty! Check if MDR is deployed!");
        }
        log.info("Nr. {} lists were cached.", cache.size());
        alreadyLoadedOnce = true;
        log.info("MDR refresh Date {}", mdrRefreshDate);
        long elapsed = stopwatch.elapsed(TimeUnit.SECONDS);
        log.info("Loading and caching took [{}] seconds!", elapsed);
    }

    private void populateAllCodeListStatuses() {
        final String OBJECT_ACRONYM = "objectAcronym";
        List<ObjectRepresentation> objectRepresentations = mdrCodeListStatus();
        objectRepresentations.forEach((objectRappr) -> {
            if (objectRappr != null && CollectionUtils.isNotEmpty(objectRappr.getFields())) {
                String objAcronym = null;
                for (ColumnDataType field : objectRappr.getFields()) {
                    final String columnName = field.getColumnName();
                    if (OBJECT_ACRONYM.equals(columnName)) {
                        objAcronym = field.getColumnValue();
                    }
                }
                allCodeLists.add(objAcronym);
            }
        });
    }

    private MdrGetAllCodeListsResponse getAllMdrCodeLists() {

        MdrGetAllCodeListsResponse allMdrCodeList = rulesMdrGateway.getAllMdrCodeList();
        if (allMdrCodeList != null) {
            return allMdrCodeList;
        }

        return null;
    }

    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public List<ObjectRepresentation> getEntry(MDRAcronymType acronymType) {
        List<ObjectRepresentation> result;
        if (acronymType != null) {
            result = cache.get(acronymType);
            if (CollectionUtils.isEmpty(result)) {
                log.warn(" Reloading {}", acronymType);
                cache.put(acronymType, mdrCodeListByAcronymType(acronymType));
                result = cache.get(acronymType);
                if (CollectionUtils.isEmpty(result)) {
                    log.error(" Failed to reload {}", acronymType);
                }
            }
            return result;
        }
        return emptyList();
    }

    /**
     * Get one mdr codeList from MDR.
     *
     * @param acronymType
     * @return
     */
    private List<ObjectRepresentation> mdrCodeListByAcronymType(MDRAcronymType acronymType) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MdrGetCodeListRequest fluxMdrGetCodeListRequest = MdrModuleMapper.createFluxMdrGetCodeListRequest(acronymType.name());
        MdrGetCodeListResponse mdrCodeList = rulesMdrGateway.getMdrCodeList(fluxMdrGetCodeListRequest);
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if (elapsed > 100) {
            log.info("Loading {} took {} ", acronymType, stopwatch);
        }
        if (mdrCodeList == null) {
            return new ArrayList<>();
        }
        return mdrCodeList.getDataSets();
    }

    private List<ObjectRepresentation> mdrCodeListStatus() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        MdrGetCodeListResponse mdrStatus = rulesMdrGateway.getMdrStatus();
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        if (elapsed > 100) {
            log.info("Loading CODELIST_STATUS took {} ", stopwatch);
        }
        if (mdrStatus == null) {
            return new ArrayList<>();
        }
        return mdrStatus.getDataSets();
    }


    /**
     * Checks if the refresh date from mdr module has changed.
     * If it has changed sets cacheDateChanged=true; and refreshed the mdrRefreshDate with the new date.
     */
    private boolean populateMdrCacheDateAndCheckIfRefreshDateChanged() throws MdrLoadingException {
        boolean cacheDateChanged = false;
        try {
            Date mdrDate = getLastTimeMdrWasRefreshedFromMdrModule();
            lastTimeRefreshDateWasRetrieved = new Date();
            if (mdrDate != null && !mdrDate.equals(mdrRefreshDate)) { // This means we have a new date from MDR module..
                mdrRefreshDate = mdrDate;
                cacheDateChanged = true;
            }
        } catch (MessageException e) {
            log.error(" Couldn't populate MDR Refresh date.. MDR Module is deployed?", e);
            throw new MdrLoadingException(e.getMessage());
        }
        return cacheDateChanged;
    }

    /**
     * Calls MDR module to get the latest date the MDR lists werisPresentInMDRListe refreshed.
     *
     * @return
     * @throws MessageException
     */
    private Date getLastTimeMdrWasRefreshedFromMdrModule() throws MessageException {
            MdrGetLastRefreshDateResponse lastRefreshDate = rulesMdrGateway.getLastRefreshDate();

            if (lastRefreshDate != null) {
                return lastRefreshDate.getLastRefreshDate() != null ? DateUtils.xmlGregorianCalendarToDate(lastRefreshDate.getLastRefreshDate()) : null;
            }

        throw new MessageException("[FATAL-ERROR] Couldn't get LastRefreshDate from MDR Module! Mdr is deployed?");
    }

    /**
     * This function overrides business rules messages to the ones defined in MDR;
     */
    private void overrideBRMessages() {
        enrichedBRMessageMap = new HashMap<>();
        final List<ObjectRepresentation> objRapprList = new ArrayList<>();
        List<ObjectRepresentation> brDef = getEntry(MDRAcronymType.FA_BR);
        List<ObjectRepresentation> saleBrDef = getEntry(MDRAcronymType.SALE_BR);
        List<ObjectRepresentation> movementDef = getEntry(MDRAcronymType.VP_BR);
        // For start up non reachable MDR purposes :)
        if (CollectionUtils.isNotEmpty(brDef)) {
            objRapprList.addAll(brDef);
        }
        if (CollectionUtils.isNotEmpty(saleBrDef)) {
            objRapprList.addAll(saleBrDef);
        }
        if (CollectionUtils.isNotEmpty(movementDef)) {
            objRapprList.addAll(movementDef);
        }

        objRapprList.removeAll(Collections.singleton(null));
        if (CollectionUtils.isEmpty(objRapprList)) {
            return;
        }
        final String MESSAGE_COLUMN = "messageIfFailing";
        final String BR_ID_COLUMN = "code";
        final String BR_NOTE_COLUMN = "note";
        final String BR_ERROR_TYPE_COLUMN = "fluxGpValidationTypeCode";
        final String CONTEXT_COLUMN = "context";
        final String ACTIVE_COLUMN = "activationIndicator";
        final String START_DATE_COLUMN = "startDate";
        final String END_DATE_COLUMN = "endDate";
        for (ObjectRepresentation objectRepr : objRapprList) {
            String brId = null;
            String errorMessage = null;
            String note = null;
            String errType = null;
            String startDate = null;
            String endDate = null;
            String context = null;
            Boolean isActive = null;
            for (ColumnDataType field : objectRepr.getFields()) {
                final String columnName = field.getColumnName();
                final String columnValue = field.getColumnValue();
                if (MESSAGE_COLUMN.equals(columnName)) {
                    errorMessage = columnValue;
                }
                if (BR_ID_COLUMN.equals(columnName)) {
                    brId = columnValue;
                }
                if (BR_NOTE_COLUMN.equals(columnName)) {
                    note = columnValue;
                }
                if (BR_ERROR_TYPE_COLUMN.equals(columnName)) {
                    errType = columnValue;
                }
                if (START_DATE_COLUMN.equals(columnName)) {
                    startDate = columnValue;
                }
                if (END_DATE_COLUMN.equals(columnName)) {
                    endDate = columnValue;
                }
                if (CONTEXT_COLUMN.equals(columnName)) {
                    context = columnValue;
                }
                if (ACTIVE_COLUMN.equals(columnName)) {
                    isActive = "Y".equals(columnValue);
                }
            }
            RuleFromMDR err = new RuleFromMDR(note, errorMessage, (errType != null && errType.contains("ERR")) ? ErrorType.ERROR.value() : ErrorType.WARNING.value(), isActive, context);
            err.setEndDate(endDate != null ? DateUtils.parseToUTCDate(endDate, "yyyy-MM-dd HH:mm:ss.S") : DateUtils.END_OF_TIME.toDate());
            err.setStartDate(startDate != null ? DateUtils.parseToUTCDate(startDate, "yyyy-MM-dd HH:mm:ss.S") : DateUtils.START_OF_TIME.toDate());
            if (enrichedBRMessageMap.get(brId) != null) {
                enrichedBRMessageMap.get(brId).add(err);
            } else {
                List<RuleFromMDR> rfmdrList = new ArrayList<>();
                rfmdrList.add(err);
                enrichedBRMessageMap.put(brId, rfmdrList);
            }
        }
    }

    public RuleFromMDR getFaBrForBrIdAndContext(String brId, String context) {
        loadAllMdrCodeLists(false);
        List<RuleFromMDR> rulesFromMdr = this.geFaBRsByBrId(brId);
        return CollectionUtils.isNotEmpty(rulesFromMdr) ? rulesFromMdr.stream().filter((ruleMdr) -> StringUtils.equals(ruleMdr.getContext(), context)).findFirst().orElse(null) : null;
    }

    public List<RuleFromMDR> getFaBrListForBrIdAndContext(String brId, String context) {
        loadAllMdrCodeLists(false);
        List<RuleFromMDR> rulesFromMdr = this.geFaBRsByBrId(brId);
        return CollectionUtils.isNotEmpty(rulesFromMdr) ? rulesFromMdr.stream().filter((ruleMdr) -> StringUtils.equals(ruleMdr.getContext(), context)).collect(Collectors.toList()) : Collections.emptyList();
    }

    public List<RuleFromMDR> geFaBRsByBrId(String brId) {
        loadAllMdrCodeLists(false);
        return enrichedBRMessageMap.get(brId);
    }

    private void populateFormatExpressions() {
        populateFormatExpressionsForList(getEntry(MDRAcronymType.FA_TRIP_ID_TYPE));
        populateFormatExpressionsForList(getEntry(MDRAcronymType.FLAP_ID_TYPE));
        populateFormatExpressionsForList(getEntry(MDRAcronymType.FLUX_GP_MSG_ID));
        populateFormatExpressionsForList(getEntry(MDRAcronymType.FLUX_VESSEL_ID_TYPE));
        log.info("Finished loading format expressions..");
    }

    private void populateFormatExpressionsForList(List<ObjectRepresentation> faTripIdList) {
        final String CODE_COLUMN = "code";
        final String FORMAT_EXPR_COLUMN = "formatExpression";
        final String START_DATE_COLUM = "startDate";
        final String END_DATE_COLUM = "endDate";
        faTripIdList.forEach((objectRappr) -> {
            String code = null;
            String expression = null;
            String startDate = null;
            String endDate = null;
            for (ColumnDataType field : objectRappr.getFields()) {
                final String columnName = field.getColumnName();
                if (CODE_COLUMN.equals(columnName)) {
                    code = field.getColumnValue();
                }
                if (FORMAT_EXPR_COLUMN.equals(columnName)) {
                    expression = field.getColumnValue();
                }
                if (START_DATE_COLUM.equals(columnName)) {
                    startDate = field.getColumnValue();
                }
                if (END_DATE_COLUM.equals(columnName)) {
                    endDate = field.getColumnValue();
                }
            }
            if (StringUtils.isNotEmpty(code)) {
                Date endDateDate = endDate != null ? DateUtils.parseToUTCDate(endDate, "yyyy-MM-dd HH:mm:ss.S") : DateUtils.END_OF_TIME.toDate();
                Date startDateDate = startDate != null ? DateUtils.parseToUTCDate(startDate, "yyyy-MM-dd HH:mm:ss.S") : DateUtils.START_OF_TIME.toDate();
                if(formatsByIdentifier.get(code) == null) {
                    FormatExpression formatExpression = new FormatExpression(expression, startDateDate, endDateDate);
                    formatsByIdentifier.put(code, new ArrayList<>(Arrays.asList(formatExpression)));
                } else {
                    List<FormatExpression> formatExpressions = formatsByIdentifier.get(code);
                    FormatExpression formatExpression = new FormatExpression(expression, startDateDate, endDateDate);
                    formatExpressions.add(formatExpression);
                    formatsByIdentifier.put(code, formatExpressions);

                }
            }
        });
    }

    private void populateDataFlowContextMappings(){
        List<ObjectRepresentation> fluxDfList = getEntry(MDRAcronymType.FLUX_DF);
        final String DATA_FLOW_COLUMN = "dataFlow";
        final String CONTEXT_COLUMN = "context";
        fluxDfList.forEach(objectRepresentation -> {
            String df = null;
            String context = null;
            for (ColumnDataType field : objectRepresentation.getFields()) {
                final String columnName = field.getColumnName();
                if (DATA_FLOW_COLUMN.equals(columnName)) {
                    df = field.getColumnValue();
                }
                if (CONTEXT_COLUMN.equals(columnName)) {
                    context = field.getColumnValue();
                }
            }
            if(StringUtils.isNoneEmpty(df)){
                if(dataFlowContexts.get(df) != null){
                    dataFlowContexts.get(df).add(context);
                } else {
                    List<String> contextList = new ArrayList<>();
                    contextList.add(context);
                    dataFlowContexts.put(df, contextList);
                }
            }
        });
        log.info("Finished loading the data flow context mappings..");
    }


    @AccessTimeout(value = 10, unit = MINUTES)
    @Lock(LockType.READ)
    public boolean isMdrCacheLoaded() {
        return cache.size() > 10;
    }

    public Map<String, List<FormatExpression>> getFormatsByIdentifier() {
        loadAllMdrCodeLists(false);
        return formatsByIdentifier;
    }

    public Map<String, List<String>> getDataFlowContexts() {
        loadAllMdrCodeLists(false);
        return dataFlowContexts;
    }
}