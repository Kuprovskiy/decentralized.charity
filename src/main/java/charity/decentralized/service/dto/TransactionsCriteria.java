package charity.decentralized.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import charity.decentralized.domain.enumeration.TransactionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link charity.decentralized.domain.Transactions} entity. This class is used
 * in {@link charity.decentralized.web.rest.TransactionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionsCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TransactionType
     */
    public static class TransactionTypeFilter extends Filter<TransactionType> {

        public TransactionTypeFilter() {
        }

        public TransactionTypeFilter(TransactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionTypeFilter copy() {
            return new TransactionTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter txid;

    private BigDecimalFilter amount;

    private TransactionTypeFilter transactionType;

    private InstantFilter createdDate;

    private BooleanFilter transactionStatus;

    private LongFilter blockHeight;

    private StringFilter key;

    private StringFilter note;

    private BigDecimalFilter longitude;

    private BigDecimalFilter latitude;

    private BigDecimalFilter humidity;

    private BigDecimalFilter temperature;

    private LongFilter projectId;

    private LongFilter userId;

    public TransactionsCriteria(){
    }

    public TransactionsCriteria(TransactionsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.txid = other.txid == null ? null : other.txid.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.transactionType = other.transactionType == null ? null : other.transactionType.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.transactionStatus = other.transactionStatus == null ? null : other.transactionStatus.copy();
        this.blockHeight = other.blockHeight == null ? null : other.blockHeight.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.humidity = other.humidity == null ? null : other.humidity.copy();
        this.temperature = other.temperature == null ? null : other.temperature.copy();
        this.projectId = other.projectId == null ? null : other.projectId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public TransactionsCriteria copy() {
        return new TransactionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTxid() {
        return txid;
    }

    public void setTxid(StringFilter txid) {
        this.txid = txid;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public TransactionTypeFilter getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeFilter transactionType) {
        this.transactionType = transactionType;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public BooleanFilter getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(BooleanFilter transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public LongFilter getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(LongFilter blockHeight) {
        this.blockHeight = blockHeight;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimalFilter humidity) {
        this.humidity = humidity;
    }

    public BigDecimalFilter getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimalFilter temperature) {
        this.temperature = temperature;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionsCriteria that = (TransactionsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(txid, that.txid) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(transactionType, that.transactionType) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(transactionStatus, that.transactionStatus) &&
            Objects.equals(blockHeight, that.blockHeight) &&
            Objects.equals(key, that.key) &&
            Objects.equals(note, that.note) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(humidity, that.humidity) &&
            Objects.equals(temperature, that.temperature) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        txid,
        amount,
        transactionType,
        createdDate,
        transactionStatus,
        blockHeight,
        key,
        note,
        longitude,
        latitude,
        humidity,
        temperature,
        projectId,
        userId
        );
    }

    @Override
    public String toString() {
        return "TransactionsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (txid != null ? "txid=" + txid + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (transactionType != null ? "transactionType=" + transactionType + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (transactionStatus != null ? "transactionStatus=" + transactionStatus + ", " : "") +
                (blockHeight != null ? "blockHeight=" + blockHeight + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (humidity != null ? "humidity=" + humidity + ", " : "") +
                (temperature != null ? "temperature=" + temperature + ", " : "") +
                (projectId != null ? "projectId=" + projectId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
