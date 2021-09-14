package com.vmware.labs.task.marketStatus.fn;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;

@ConfigurationProperties( prefix = "date-to-market-status" )
public class DateToMarketStatusConfigProperties {

    private String status = "OPEN";

    /**
     * Format for the date value.
     */
    private String dateFormat = "MM/dd/yy HH:mm:ss";

    @NotEmpty
    public String getStatus() {

        return status;
    }

    public void setStatus( final String status ) {

        this.status = status;

    }

    @DateFormat
    public String getDateFormat() {

        return this.dateFormat;
    }

    public void setDateFormat( final String dateFormat ) {

        this.dateFormat = dateFormat;

    }

}
