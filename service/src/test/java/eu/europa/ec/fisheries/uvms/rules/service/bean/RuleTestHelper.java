package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;

import java.math.BigDecimal;

/**
 * Created by sanera on 10/05/2017.
 */
public class RuleTestHelper {

    public static RuleType createRuleType(String expression, String brId, String note, ErrorType type, String errorMessage){
        RuleType ruleType = new RuleType();
        ruleType.setExpression(expression);
        ruleType.setBrId(brId );
        ruleType.setNote(note);
        ruleType.setErrorType(type);
        ruleType.setMessage(errorMessage);
        ruleType.setLevel("LevelName");

        return ruleType;
    }

    public static CodeType getCodeType(String value, String listId){
        CodeType codeType = new CodeType();
        codeType.setValue(value);
        codeType.setListId(listId);

        return codeType;
    }

    public static MeasureType getMeasureType(BigDecimal value, String unitCode){
        MeasureType measureType = new MeasureType();
        measureType.setValue(value);
        measureType.setUnitCode(unitCode);
        return measureType;
    }

    public static IdType getIdType(String value, String schemeId){
        IdType idType = new IdType();
        idType.setValue(value);
        idType.setSchemeId(schemeId);
        return idType;
    }
}
