package co.ke.tinker.http;

import co.ke.tinker.exception.ExceptionCode;
import co.ke.tinker.exception.NetworkException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClient {
    private final OkHttpClient client;
    private static final int TIMEOUT_SECONDS = 30;

    public HttpClient() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    public HttpResponse get(String url, Map<String, String> headers) {
        try {
            Request.Builder requestBuilder = new Request.Builder().url(url).get();
            applyHeaders(requestBuilder, headers);
            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            return toHttpResponse(response);
        } catch (IOException e) {
            throw new NetworkException("Network error: " + e.getMessage(), ExceptionCode.NETWORK_ERROR, e);
        }
    }

    public HttpResponse post(String url, Map<String, String> headers, String body) {
        try {
            RequestBody requestBody = body != null ? RequestBody.create(body, MediaType.parse("application/json")) : null;
            Request.Builder requestBuilder = new Request.Builder().url(url);
            applyHeaders(requestBuilder, headers);

            if (requestBody != null) {
                requestBuilder.post(requestBody);
            } else {
                requestBuilder.post(RequestBody.create("", null));
            }

            Request request = requestBuilder.build();
            Response response = client.newCall(request).execute();
            return toHttpResponse(response);
        } catch (IOException e) {
            throw new NetworkException("Network error: " + e.getMessage(), ExceptionCode.NETWORK_ERROR, e);
        }
    }

    private void applyHeaders(Request.Builder requestBuilder, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private HttpResponse toHttpResponse(Response response) throws IOException {
        Map<String, java.util.List<String>> responseHeaders = new HashMap<>();
        Headers responseHeadersObj = response.headers();
        for (String name : responseHeadersObj.names()) {
            responseHeaders.put(name, responseHeadersObj.values(name));
        }

        String responseBody = response.body() != null ? response.body().string() : "";
        return new HttpResponse(response.code(), responseBody, responseHeaders);
    }
}
