package charity.decentralized.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link charity.decentralized.domain.Transactions} entity.
 */
public class BloqlyTransactionsDTO implements Serializable {

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal humidity;

    private BigDecimal temperature;

    @NotNull
    private Long projectId;


    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloqlyTransactionsDTO that = (BloqlyTransactionsDTO) o;
        return Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(humidity, that.humidity) &&
            Objects.equals(temperature, that.temperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, humidity, temperature);
    }

    @Override
    public String toString() {
        return "{" +
            "longitude=" + longitude +
            ", latitude=" + latitude +
            ", humidity=" + humidity +
            ", temperature=" + temperature +
            '}';
    }
}
