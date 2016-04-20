package com.device42.client.services;

import com.device42.client.util.Device42ClientException;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class Device42ClientFactory {
	private static Logger logger = Logger.getLogger(Device42ClientFactory.class);

	public static DevicesRestClient createDeviceClient(String baseUrl, String username, String password) throws URISyntaxException {
		return new DevicesRestClient(baseUrl, createHttpClient(username, password));
	}

	private static CloseableHttpClient createHttpClient(String username, String password) {
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
