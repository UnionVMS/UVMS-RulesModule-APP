package eu.europa.ec.fisheries.uvms.rules.model.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmType;

public class AlarmListResponseDto implements Serializable {

    private static final long serialVersionUID = 1;

    private List<AlarmType> alarmList;
    private BigInteger totalNumberOfPages;
    private BigInteger currentPage;

    public List<AlarmType> getAlarmList() {
        return alarmList;
    }

    public void setAlarmList(List<AlarmType> alarmList) {
        this.alarmList = alarmList;
    }

    public BigInteger getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(BigInteger totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public BigInteger getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(BigInteger currentPage) {
        this.currentPage = currentPage;
    }

}
