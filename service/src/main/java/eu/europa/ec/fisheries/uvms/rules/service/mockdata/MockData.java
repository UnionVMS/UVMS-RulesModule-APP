package eu.europa.ec.fisheries.uvms.rules.service.mockdata;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;

public class MockData {

    /**
     * Get mocked data single object
     *
     * @param id
     * @return
     */
    public static CustomRuleType getDto(Long id) {
        CustomRuleType dto = new CustomRuleType();
        dto.setName("DummyRule" + id);
        dto.setAvailability(AvailabilityType.PUBLIC);
        return dto;
    }

    /**
     * Get mocked data as a list
     *
     * @param amount
     * @return
     */
    public static List<CustomRuleType> getDtoList(Integer amount) {
        List<CustomRuleType> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return dtoList;
    }

}
