package eu.europa.ec.fisheries.uvms.rules.service.mockdata;

import eu.europa.ec.fisheries.wsdl.types.ModuleObject;
import java.util.ArrayList;
import java.util.List;

public class MockData {

    /**
     * Get mocked data single object
     *
     * @param id
     * @return
     */
    public static ModuleObject getDto(Long id) {
        ModuleObject dto = new ModuleObject();
        dto.setId(id.toString());
        return null;
    }

    /**
     * Get mocked data as a list
     *
     * @param amount
     * @return
     */
    public static List<ModuleObject> getDtoList(Integer amount) {
        List<ModuleObject> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return null;
    }

}
