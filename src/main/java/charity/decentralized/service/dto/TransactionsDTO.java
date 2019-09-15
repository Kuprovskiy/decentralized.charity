package charity.decentralized.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import charity.decentralized.domain.enumeration.TransactionType;

/**
 * A DTO for the {@link charity.decentralized.domain.Transactions} entity.
 */
public class TransactionsDTO implements Serializable {

    private Long id;

    @NotNull
    private String txid;

    private BigDecimal amount;

    private TransactionType transactionType;

    @NotNull
    private Instant createdDate;

    private Boolean transactionStatus;

    private Long blockHeight;

    private String key;

    private String note;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private BigDecimal humidity;

    private BigDecimal temperature;


    private Long projectId;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionsDTO transactionsDTO = (TransactionsDTO) o;
        if (transactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionsDTO{" +
            "id=" + getId() +
            ", txid='" + getTxid() + "'" +
            ", amount=" + getAmount() +
            ", transactionType='" + getTransactionType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", transactionStatus='" + isTransactionStatus() + "'" +
            ", blockHeight=" + getBlockHeight() +
            ", key='" + getKey() + "'" +
            ", note='" + getNote() + "'" +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", humidity=" + getHumidity() +
            ", temperature=" + getTemperature() +
            ", project=" + getProjectId() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
