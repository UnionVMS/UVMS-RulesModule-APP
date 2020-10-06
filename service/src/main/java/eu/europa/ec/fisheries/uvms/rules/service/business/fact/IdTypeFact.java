package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;

import java.util.Arrays;

@Data
public class IdTypeFact extends AbstractFact {

    private IdType id;

    public IdTypeFact() {
        super();
        setFactType();
    }

    public IdTypeFact(IdType id) {
        this.id = id;
        setFactType();
    }

    /**
     * Method to validate the last digit of the schemeId Value if schemeId type is UVI
     *
     * @param schemeIdValue the value of the schemeId
     * @return true if the validation fails and false if succeeds
     * Extract the digits from the String SchemeIdValue
     * iterate through them  to calculate the sum of each one being
     * successively (individually) multiplied by 7,6,5,4,3,2.
     * Last digit of the calculated sum is the 7th digit.
     * see(14.5. Validation of the IMO number format (UVI) v2.5)
     */
    public boolean isLastCheckBitInvalidInUVISchemeId(String schemeIdValue) {
        int[] digits = Arrays.stream(schemeIdValue.split("")).mapToInt(Integer::parseInt).toArray();
        if (digits.length == 7) {
            int lastDigit = digits[digits.length - 1];
            int sum = 0;
            for (int i = 1; i < digits.length; i++) {
                sum += (digits.length + 1 - i) * digits[i - 1];
            }
            int lastDigitOfSum = Math.abs(sum) % 10;

            return lastDigitOfSum != lastDigit;
        }
        return true;
    }


    @Override
    public void setFactType() {
        this.factType = FactType.SIMPLE_ID_TYPE_FACT;
    }
}
