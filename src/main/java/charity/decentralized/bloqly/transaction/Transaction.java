package charity.decentralized.bloqly.transaction;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Transaction {

    private String space;

    private String key;

    private Long nonce;

    private Long height;

    private Long timestamp;

    private String memo;

    private List<String> tags;

    private String value;

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] toMessage() {
        try {
            ByteArrayOutputStream tagBytes = new ByteArrayOutputStream();

            Collections.sort(tags);

            for (String tag : tags) {
                tagBytes.write(tag.getBytes(UTF_8));
            }

            ByteArrayOutputStream messageBytes = new ByteArrayOutputStream();

            messageBytes.write(space.getBytes(UTF_8));
            messageBytes.write(key.getBytes(UTF_8));
            messageBytes.write(ByteBuffer.allocate(Long.BYTES).putLong(nonce).array());
            messageBytes.write(ByteBuffer.allocate(Long.BYTES).putLong(timestamp).array());
            messageBytes.write(memo.getBytes(UTF_8));
            messageBytes.write(tagBytes.toByteArray());
            messageBytes.write(value.getBytes(UTF_8));

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            return messageDigest.digest(messageBytes.toByteArray());

        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(space, that.space) &&
            Objects.equals(key, that.key) &&
            Objects.equals(nonce, that.nonce) &&
            Objects.equals(height, that.height) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(memo, that.memo) &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(space, key, nonce, height, timestamp, memo, tags, value);
    }
}
