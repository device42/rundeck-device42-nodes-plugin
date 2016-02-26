package com.device42.client.services;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.log4j.Logger;

import com.device42.client.util.Device42ClientException;

public class Device42ClientFactory {
    private static Logger logger = Logger.getLogger(Device42ClientFactory.class);

    public static BuildingsRestClient createBuildingClient(String baseUrl, String username, String password) {
        return new BuildingsRestClient(baseUrl, createHttpClient(username, password));
    }

    public static DevicesRestClient createDeviceClient(String baseUrl, String username, String password) {
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

    public static PartsRestClient createPartsClient(String baseUrl, String username, String password) {
        return new PartsRestClient(baseUrl, createHttpClient(username, password));
    }

    public static PDUsRestClient createPDUsClient(String baseUrl, String username, String password) {
        return new PDUsRestClient(baseUrl, createHttpClient(username, password));
    }

    public static RacksRestClient createRackClient(String baseUrl, String username, String password) {
        return new RacksRestClient(baseUrl, createHttpClient(username, password));
    }

    public static RoomsRestClient createRoomClient(String baseUrl, String username, String password) {
        return new RoomsRestClient(baseUrl, createHttpClient(username, password));
    }

    public static ServicesRestClient createServiceClient(String baseUrl, String username, String password) {
        return new ServicesRestClient(baseUrl, createHttpClient(username, password));
    }

    private static SSLContext createSslContext() {
        try {
            return new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                    return true;
                }
            }).build();
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
