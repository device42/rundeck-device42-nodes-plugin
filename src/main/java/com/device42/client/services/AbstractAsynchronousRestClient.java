package com.device42.client.services;

import com.device42.client.parser.BasicErrorJsonParser;
import com.device42.client.parser.JsonObjectListParser;
import com.device42.client.parser.JsonObjectParser;
import com.device42.client.services.parameters.InputLimitParameters;
import com.device42.client.services.parameters.InputParameters;
import com.device42.client.util.Device42ClientException;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.Closeable;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 * Basic REST API Client routines such as establishing connection and conversion
 * of result into the specified model
 */
abstract class AbstractAsynchronousRestClient implements Closeable {
	/**
	 * Used to log the error data for the connection
	 */
	private static Logger logger = Logger.getLogger(AbstractAsynchronousRestClient.class);

	/**
	 * The HTTP host of Device42 application
	 */
	private final HttpHost targetHost;
	/**
	 * The HTTP client with the authentication information
	 */
	private final CloseableHttpClient httpClient;
	/**
	 * The additional context information for the queries
	 */
	private final HttpClientContext clientContext;

	/**
	 * Create the instance with the information regarding Device42 application
	 * host and client with credentials
	 * 
	 * @param baseUrl
	 *            URL of the Device42 application
	 * @param httpClient
	 *            The HTTP client with credentials information
	 */
	protected AbstractAsynchronousRestClient(String baseUrl, CloseableHttpClient httpClient) {
		this.targetHost = HttpHost.create(baseUrl);
		this.httpClient = httpClient;
		this.clientContext = createClientContext();
	}

	/**
	 * Close the connection
	 */
	@Override
	public void close() throws IOException {
		httpClient.close();
	}

	/**
	 * Create the default HTTP context with the parameters needed for the
	 * authentication at Device42 application
	 * 
	 * @return context for the queries
	 */
	private HttpClientContext createClientContext() {
		AuthCache authCache = new BasicAuthCache();
		authCache.put(targetHost, new BasicScheme());
		HttpClientContext clientContext = HttpClientContext.create();
		clientContext.setAuthCache(authCache);
		return clientContext;
	}

	/**
	 * Collect the data from the REST API and convert it using the parser into
	 * the model. This method will follow the rules of the Device42 server and
	 * will use limit and offset to return the subset of data if can be applied
	 * 
	 * @param path
	 *            The path to the REST API endpoint
	 * @param parser
	 *            The converter from JSON Object into the model entities
	 * @param inputParameters
	 *            The key-value parameters of the request
	 * @return The request result parsed into the model
	 */
	protected <T> T get(String path, JsonObjectParser<T> parser, InputParameters inputParameters) {
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
			throw new Device42ClientException(ex.getMessage(), ex);
		} catch (JSONException ex) {
			logger.error("JSON error", ex);
			throw new Device42ClientException(ex.getMessage(), ex);
		}
	}

	/**
	 * Collect all the data from the list by the set of subsequent queries to
	 * the REST API endpoint
	 * 
	 * @param path
	 *            The path to the REST API endpoint
	 * @param parser
	 *            The converter from JSON Object into the model entities
	 * @param inputParameters
	 *            The key-value parameters of the request
	 * @return The request result parsed into the model
	 */
	protected <T> List<T> getAll(String path, JsonObjectListParser<T> parser, InputLimitParameters inputParameters) {
		List<T> result = get(path, parser, inputParameters);
		if (result != null && parser.getLimit() > 0 && parser.getCount() > 0 && parser.getCount() > result.size()) {
			for (int offset = parser.getLimit(); offset < parser.getCount(); offset += parser.getLimit()) {
				inputParameters.addLimit(parser.getLimit());
				inputParameters.addOffset(offset);
				List<T> partialResult = get(path, parser, inputParameters);
				result.addAll(partialResult);
			}
		}
		return result;
	}

	/**
	 * Create the HTTP clean with basic authentication mechanism using specified
	 * credentials
	 * 
	 * @param username
	 *            Authentication username
	 * @param password
	 *            Authentication password
	 * @return
	 */
	protected static CloseableHttpClient createHttpClient(String username, String password) {
		SSLContext sslContext = createSslContext();
		HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
		SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

		Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		List<Header> headers = Arrays.asList(header);

		return HttpClients.custom()
				.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.setSSLSocketFactory(sslSocketFactory)
				.setDefaultCredentialsProvider(credentialsProvider)
				.setDefaultHeaders(headers)
				.build();
	}

	/**
	 * Create the SSL Context for the secured connection
	 * 
	 * @return SSL Context for the secured connection
	 */
	private static SSLContext createSslContext() {
		try {
			return new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error creating SSL context", e);
			throw new Device42ClientException("Error creating SSL context. No such algorithm.");
		} catch (KeyManagementException e) {
			logger.error("Error creating SSL context", e);
			throw new Device42ClientException("Error creating SSL context. Key manager exception.");
		} catch (KeyStoreException e) {
			logger.error("Error creating SSL context", e);
			throw new Device42ClientException("Error creating SSL context. Keystore exception.");
		}
	}
}
