package com.device42.client.services;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.client.AuthCache;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.device42.client.parser.BasicErrorJsonParser;
import com.device42.client.parser.JsonObjectParser;
import com.device42.client.parser.JsonParser;
import com.device42.client.services.parameters.EmptyInputParameters;
import com.device42.client.services.parameters.InputParameters;
import com.device42.client.util.Device42ClientException;

abstract class AbstractAsynchronousRestClient implements Closeable {
    private static Logger logger = Logger.getLogger(AbstractAsynchronousRestClient.class);

    private final HttpHost targetHost;
    private final CloseableHttpClient httpClient;
    private final HttpClientContext clientContext;

    protected AbstractAsynchronousRestClient(String baseUrl, CloseableHttpClient httpClient) {
        this.targetHost = HttpHost.create(baseUrl);
        this.httpClient = httpClient;
        this.clientContext = createClientContext();
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    private HttpClientContext createClientContext() {
        AuthCache authCache = new BasicAuthCache();
        authCache.put(targetHost, new BasicScheme());
        HttpClientContext clientContext = HttpClientContext.create();
        clientContext.setAuthCache(authCache);
        return clientContext;
    }

    protected <T> T get(String path, JsonParser<?, T> parser) {
        return get(path, parser, new EmptyInputParameters());
    }

    @SuppressWarnings("unchecked")
    protected <T> T get(String path, JsonParser<?, T> parser, InputParameters inputParameters) {
        RequestBuilder requestBuilder = RequestBuilder.get().setUri(path);
        for (Map.Entry<String, String> entry : inputParameters.parametersMap().entrySet()) {
            requestBuilder.addParameter(entry.getKey(), entry.getValue());
        }
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(targetHost, requestBuilder.build(), clientContext);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() >= 200 && statusLine.getStatusCode() < 300) {
                JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                return (T) ((JsonObjectParser<T>) parser).parse(jsonObject);
            } else {
                String errorMessage = "";
                try {
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    errorMessage = new BasicErrorJsonParser().parse(jsonObject).getMessage();
                } catch (JSONException ex) {
                    errorMessage = statusLine.getStatusCode() + ": " + statusLine.getReasonPhrase();
                }
                logger.error("Unexpected response status: " + errorMessage);
                throw new Device42ClientException(errorMessage);
            }
        } catch (IOException ex) {
            logger.error("Error to call REST API", ex);
            throw new Device42ClientException(ex.getMessage());
        } catch (JSONException ex) {
            logger.error("JSON error", ex);
            throw new Device42ClientException(ex.getMessage());
        }
    }
}
