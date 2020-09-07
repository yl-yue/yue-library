package ai.yue.library.base.config.net.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * 跳过SSL证书校验，{@linkplain org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet.SkipSslVerificationHttpRequestFactory}
 * <p>{@link SimpleClientHttpRequestFactory} that skips SSL certificate verification.
 * <p>信任自签证书，跳过Hostname检查
 * 
 * @author	ylyue
 * @since	2018年11月10日
 */
public class SkipSslVerificationHttpRequestFactory extends SimpleClientHttpRequestFactory {

	@Override
	protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
		if (connection instanceof HttpsURLConnection) {
			prepareHttpsConnection((HttpsURLConnection) connection);
		}
		super.prepareConnection(connection, httpMethod);
	}
	
	private void prepareHttpsConnection(HttpsURLConnection connection) {
		connection.setHostnameVerifier(new SkipHostnameVerifier());
		try {
			connection.setSSLSocketFactory(SkipSslVerificationHttpRequestFactory.getSSLContext().getSocketFactory());
		} catch (Exception ex) {
			// Ignore
		}
	}
	
	/**
	 * 信任自签证书
	 */
	public static SSLContext getSSLContext() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new SkipX509TrustManager() }, new SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			e.printStackTrace();
		}
		
		return sslContext;
	}
	
	private static class SkipX509TrustManager implements X509TrustManager {

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

	}
	
}
