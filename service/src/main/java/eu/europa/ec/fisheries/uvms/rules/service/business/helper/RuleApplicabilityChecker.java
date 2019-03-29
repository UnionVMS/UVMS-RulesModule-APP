package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleFromMDR;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

public class RuleApplicabilityChecker {

    public static boolean isApplicable(String brId, String ruleDataFlow, String messageDataFlow, DateTime messageCreationDate, MDRCacheRuleService mdrService){
       // List<String> dfListForBrId = mdrService.getDataFlowListForBRId(brId);
        List<RuleFromMDR> mdrRulesList = mdrService.getFaBrsForBrId(brId); // mdrService.getFaBrsForBrIdAndDataFlow(brId, messageDataFlow);
        if(CollectionUtils.isEmpty(mdrRulesList)){ // 1.  Rule doesn't exist in MDR
           return false;
        }
        RuleFromMDR chosenRelevantRule = choseRelevantDataFlow(ruleDataFlow, messageDataFlow, mdrRulesList);
        boolean isApplicable = false;
        if(chosenRelevantRule != null) { // 1.
            // This date in case of FA_Messages come from FA_REPORT or FA_MESSAGE
            Date startDate = chosenRelevantRule.getStartDate();
            Date endDate = chosenRelevantRule.getEndDate();
            if (chosenRelevantRule.isActive() && isDateInRange(messageCreationDate, startDate, endDate) ) { //&& isDataFlowRelevan(...,...,...)
                isApplicable = true;
            }
        }
        return isApplicable;
    }

    private static RuleFromMDR choseRelevantDataFlow(String ruleDataFlow, String messageDataFlow, List<RuleFromMDR> mdrRulesList) {

        // A perfect match can be found
        if(StringUtils.equals(ruleDataFlow, messageDataFlow)){
            RuleFromMDR chosenOne = null;
            for (RuleFromMDR ruleFromMDR : mdrRulesList) {
                if(StringUtils.equals(ruleFromMDR.getDataFlow(), messageDataFlow)){
                    chosenOne = ruleFromMDR;
                }
            }
            return chosenOne; // if chosenOne is null => doesn't exist in MDR
        }

        // A perfect match cannot be found so we apply this only if a rule with dataFlow="null" has been defined in rules and in MDR
        if(StringUtils.isEmpty(ruleDataFlow)){
            for (RuleFromMDR ruleFromMDR : mdrRulesList) {
                if(StringUtils.isEmpty(ruleFromMDR.getDataFlow())){
                    return ruleFromMDR;
                }
            }
        }
        return null;
    }

    /**
     * Example :
     *
     *  Rule001     DF1    EXPR1
     *  Rule001     NULL   EXPR1
     *  Rule001     DF2    EXPR1
     *
     *  We'll have 3 rules in the KieContainer for this, since it is flattenmed there!
     *
     *  And the message coming in has a DF1
     *
     *     1)
     *        ruleDataFlow="DF1";
     *        messageDataFlow="DF1";
     *        dfListForBrId={"DF1","NULL","DF2"};
     *
     *        isDataFlowRelevant("DF1", "DF1", {"DF1","NULL","DF2"})    => TRUE
     *
     *     2)
     *        ruleDataFlow="NULL";
     *        messageDataFlow="DF1";
     *        dfListForBrId={"DF1","NULL","DF2"};
     *
     *        isDataFlowRelevant("NULL", "DF1", {"DF1","NULL","DF2"})    => FALSE
     *
     *     3)
     *        ruleDataFlow="DF2";
     *        messageDataFlow="DF1";
     *        dfListForBrId={"DF1","NULL","DF2"};
     *
     *        isDataFlowRelevant("DF2", "DF1", {"DF1","NULL","DF2"})    => FALSE
     *
     *  And the message coming in has a DF3
     *
     * @param ruleDataFlow (can be null)
     * @param messageDataFlow
     * @param dfListForBrId
     * @return
     */
    private static boolean isDataFlowRelevan(String ruleDataFlow, String messageDataFlow, List<String> dfListForBrId){
        return StringUtils.equals(ruleDataFlow, messageDataFlow) || ((ruleDataFlow == null) && !dfListForBrId.contains(messageDataFlow));
    }

    private static boolean isDateInRange(DateTime jodaTestDate, Date startDate, Date endDate) {
        Date testDate = jodaTestDate.toDate();
        return !(testDate.before(startDate) || testDate.after(endDate));
    }

}
