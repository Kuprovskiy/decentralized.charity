package charity.decentralized.bloqly;


import charity.decentralized.bloqly.transaction.SignedTransaction;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BloqlyClient {

    private static final String NODE_LAST_STATE_API = "api/events/last-state";

    private static final String NODE_SUBMIT_TX_API = "api/events/add";

    private static final OkHttpClient client = new OkHttpClient();

    private final String nodeUrl;

    public BloqlyClient(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public Optional<SignedTransaction> getLastTransaction(String space, String key) {

        HttpUrl url = HttpUrl.parse(nodeUrl)
            .newBuilder()
            .addPathSegments(NODE_LAST_STATE_API)
            .addEncodedQueryParameter("space", space)
            .addEncodedQueryParameter("key", key)
            .build();

        Request request = new Request.Builder()
            .url(url)
            .get()
            .build();

        try (Response response = client.newCall(request).execute()) {

            if (response.code() == 404) {
                return Optional.empty();
            }

            if (!response.isSuccessful()) {
                throw new IllegalStateException(
                    String.format("Could not get last transaction: %s, %s", response.message(), response.code()));
            }

            ResponseBody body = response.body();

            if (body == null) {
                throw new IllegalStateException("Response body is empty");
            }

            InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(body.bytes()));

            return Optional.of(ObjectUtils.readValue(reader, SignedTransaction.class));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void submitTransaction(SignedTransaction tx) {

        try {
            String txPayload = ObjectUtils.writeValueAsString(tx);

            String txEncoded = Base64.getEncoder().encodeToString(txPayload.getBytes(UTF_8));

            String txPrepared = URLEncoder.encode(txEncoded, UTF_8.name());

            HttpUrl url = HttpUrl.parse(nodeUrl)
                .newBuilder()
                .addPathSegments(NODE_SUBMIT_TX_API)
                .addEncodedQueryParameter("event", txPrepared)
                .build();

            Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

            try (Response response = client.newCall(request).execute()) {

                if (!response.isSuccessful()) {
                    throw new IllegalStateException(
                        String.format("Could not submit transaction for event: %s, response: %s ", tx, response));
                }

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
