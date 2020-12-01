package com.vmware.labs.task.marketStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class MarketStatusDomainEvent {

    private final String status;
    private final LocalDateTime occurred;

    @JsonCreator
    public MarketStatusDomainEvent(
            @JsonProperty final String status,
            @JsonProperty final LocalDateTime occurred) {

        this.status = status;
        this.occurred = occurred;

    }

    @JsonProperty( "status" )
    public String getStatus() {

        return status;
    }

    @JsonProperty( "occurred" )
    public LocalDateTime getOccurred() {

        return occurred;
    }

    @Override
    public String toString() {

        return "MarketStatusDomainEvent{" +
                "status='" + status + '\'' +
                ", occurred=" + occurred +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketStatusDomainEvent that = (MarketStatusDomainEvent) o;
        return status.equals(that.status) &&
                occurred.equals(that.occurred);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, occurred);
    }

}
