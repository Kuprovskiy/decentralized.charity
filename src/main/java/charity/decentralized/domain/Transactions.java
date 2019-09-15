package charity.decentralized.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import charity.decentralized.domain.enumeration.TransactionType;

/**
 * A Transactions.
 */
@Entity
@Table(name = "transactions")
public class Transactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "txid", nullable = false)
    private String txid;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "transaction_status")
    private Boolean transactionStatus;

    @Column(name = "block_height")
    private Long blockHeight;

    @Column(name = "jhi_key")
    private String key;

    @Column(name = "note")
    private String note;

    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    @Column(name = "latitude", precision = 21, scale = 2)
    private BigDecimal latitude;

    @Column(name = "humidity", precision = 21, scale = 2)
    private BigDecimal humidity;

    @Column(name = "temperature", precision = 21, scale = 2)
    private BigDecimal temperature;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTxid() {
        return txid;
    }

    public Transactions txid(String txid) {
        this.txid = txid;
        return this;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Transactions amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Transactions transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Transactions createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean isTransactionStatus() {
        return transactionStatus;
    }

    public Transactions transactionStatus(Boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public void setTransactionStatus(Boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public Transactions blockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
        return this;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getKey() {
        return key;
    }

    public Transactions key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNote() {
        return note;
    }

    public Transactions note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Transactions longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public Transactions latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public Transactions humidity(BigDecimal humidity) {
        this.humidity = humidity;
        return this;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public Transactions temperature(BigDecimal temperature) {
        this.temperature = temperature;
        return this;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public Project getProject() {
        return project;
    }

    public Transactions project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public Transactions user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transactions)) {
            return false;
        }
        return id != null && id.equals(((Transactions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Transactions{" +
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
            "}";
    }
}
