package eu.europa.ec.fisheries.uvms.rules.service.business.helper;

import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleFromMDR;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.Date;


@Stateless
@LocalBean
@Slf4j
public class RuleApplicabilityChecker {

    /**
     * Most important parameters :
     * <p>
     * 1. thisRulesBrId of this Rule
     * 2. MSG's DF
     * 3. Rules's DF
     * <p>
     * A Rule is deemed relevant if :
     * <p>
     * 1. Exists in MDR
     * 2. Is Active in MDR
     * 3. DF of that rule is relevant (either null or == to msg's df ** If its null then its relevant only if it doesn't exist another rule in MDR (with the same thisRulesBrId ovv..) that has the same DF as the message)
     * 4. The Rule in MDR is not expired
     * 5. The country is relevant
     * <p>
     * 1. Get the FA_BR definition for the specific thisRulesBrId (this rule's thisRulesBrId)
     * 1.1 If that doesn't exist => Rule doesn't exist in MDR
     *
     * @param thisRulesBrId
     * @param ruleContext
     * @param messageDataFlow
     * @param messageCreationDate
     * @param mdrService
     * @return
     */
    public boolean isApplicable(String thisRulesBrId, String ruleContext, String messageDataFlow, DateTime messageCreationDate, MDRCacheRuleService mdrService) {
        String msgContext = mdrService.findContextForDf(messageDataFlow);
        if (StringUtils.isEmpty(msgContext)) {
            return false;
        }
        RuleFromMDR faBrForBrIdAndContext = null;
        if (StringUtils.isNotEmpty(msgContext)) {
            faBrForBrIdAndContext = mdrService.getFaBrForBrIdAndContext(thisRulesBrId, msgContext);
            if (faBrForBrIdAndContext == null) {
                faBrForBrIdAndContext = mdrService.getFaBrForBrIdAndContext(thisRulesBrId, null);
            }
        }
        if (StringUtils.isEmpty(ruleContext) || "NULL".equals(ruleContext)) { // Case : rule.context == null. Rule is applicable only if for this msg.context and this thisRulesBrId cannot be found a rule in rules table.
            if (!mdrService.doesRuleExistInRulesTable(thisRulesBrId, msgContext)) {
                return checkMdrRuleValidity(messageCreationDate, faBrForBrIdAndContext);
            }
        } else {// Case : rule.context != null and == msg.context. We apply this rule only if a perfect match can be found in MDR
            if (StringUtils.equals(ruleContext, msgContext)) {
                return checkMdrRuleValidity(messageCreationDate, faBrForBrIdAndContext);
            }
        }
        return false;
    }

    private boolean checkMdrRuleValidity(DateTime messageCreationDate, RuleFromMDR faBrForBrIdAndContext) {
        return faBrForBrIdAndContext != null
                && faBrForBrIdAndContext.isActive()
                && isDateInRange(messageCreationDate, faBrForBrIdAndContext.getStartDate(), faBrForBrIdAndContext.getEndDate())
                && isRuleApplicableForCountry();
    }

    /**
     *  TODO : When MDR's <FA_BR.country> field is ready => implement me!
     *
     * @return
     */
    private boolean isRuleApplicableForCountry() {
        return true;
    }

    private boolean isDateInRange(DateTime jodaTestDate, Date startDate, Date endDate) {
        if (jodaTestDate == null || startDate == null || endDate == null) {
            return true;
        }
        Date testDate = jodaTestDate.toDate();
        return testDate.after(startDate) && testDate.before(endDate);
    }

}