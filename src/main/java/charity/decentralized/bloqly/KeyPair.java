package charity.decentralized.bloqly;

import charity.decentralized.bloqly.transaction.SignedTransaction;
import charity.decentralized.bloqly.transaction.Transaction;
import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class KeyPair {

    private static final int KEY_SIZE = 32;

    private final byte[] privateKey;

    private final byte[] publicKey = new byte[KEY_SIZE];

    public String getPublicKeyEncoded() {
        return Base64.getEncoder().encodeToString(publicKey);
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    private KeyPair(byte[] privateKey) {

        if (privateKey.length != KEY_SIZE) {
            throw new IllegalArgumentException(
                String.format("Invalid private key length: %s, expected: %s", privateKey.length, KEY_SIZE));
        }

        this.privateKey = privateKey;
        Ed25519.generatePublicKey(privateKey, 0, publicKey, 0);
    }

    public static KeyPair fromPrivateKeyEncoded(String privateKeyEncoded) {
        return new KeyPair(Base64.getDecoder().decode(privateKeyEncoded.getBytes(UTF_8)));
    }

    public static KeyPair fromPrivateKey(byte[] privateKey) {
        return new KeyPair(privateKey);
    }

    public SignedTransaction signTransaction(Transaction tx) {
        byte[] message = tx.toMessage();

        byte[] signature = new byte[Ed25519.SIGNATURE_SIZE];

        Ed25519.sign(privateKey, 0, message, 0, message.length, signature, 0);

        SignedTransaction signedTx = new SignedTransaction(tx);

        signedTx.setHash(Hex.toHexString(message));
        signedTx.setPublicKey(getPublicKeyEncoded());
        signedTx.setSignature(Base64.getEncoder().encodeToString(signature));

        return signedTx;
    }
}
