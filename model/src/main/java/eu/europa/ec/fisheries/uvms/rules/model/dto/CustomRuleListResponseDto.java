package eu.europa.ec.fisheries.uvms.rules.model.dto;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;

import java.io.Serializable;
import java.util.List;

public class CustomRuleListResponseDto implements Serializable {

    private static final long serialVersionUID = 1;

    private List<CustomRuleType> customRuleList;
    private int totalNumberOfPages;
    private int currentPage;

    public List<CustomRuleType> getCustomRuleList() {
        return customRuleList;
    }

    public void setCustomRuleList(List<CustomRuleType> customRuleList) {
        this.customRuleList = customRuleList;
    }

    public int getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(int totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
