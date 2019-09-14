package charity.decentralized.bloqly.transaction;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;
import java.util.Objects;

public class SignedTransaction extends Transaction {
    private String hash;

    private String publicKey;

    private String signature;

    public SignedTransaction() {
    }

    public SignedTransaction(Transaction tx) {
        setSpace(tx.getSpace());
        setKey(tx.getKey());
        setNonce(tx.getNonce());
        setHeight(tx.getHeight());
        setHeight(tx.getHeight());
        setTimestamp(tx.getTimestamp());
        setMemo(tx.getMemo());
        setTags(new ArrayList<>(tx.getTags()));
        setValue(tx.getValue());
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @JsonAlias("publicKey")
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignedTransaction)) return false;
        if (!super.equals(o)) return false;
        SignedTransaction that = (SignedTransaction) o;
        return Objects.equals(hash, that.hash) &&
            Objects.equals(publicKey, that.publicKey) &&
            Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hash, publicKey, signature);
    }
}
