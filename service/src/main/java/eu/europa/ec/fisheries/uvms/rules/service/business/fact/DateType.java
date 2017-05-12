package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by sanera on 11/05/2017.
 */
@NoArgsConstructor
public class DateType {
    Date date;
    String format;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
