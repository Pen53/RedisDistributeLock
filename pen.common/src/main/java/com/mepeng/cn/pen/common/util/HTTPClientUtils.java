package com.mepeng.cn.pen.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

public class HTTPClientUtils {

	static final int timeOut = 600 * 1000;

	public static CloseableHttpClient httpClient;

	public final static Object syncLock = new Object();

	public static void config(HttpRequestBase httpRequestBase) {
		// 配置请求的超时设置
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
		httpRequestBase.setConfig(requestConfig);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> sendGets(String url) throws Exception {
		try {

			HttpGet httpGet = new HttpGet();

			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
			httpGet.setConfig(requestConfig);

			httpGet.setURI(new URI(url));

			CloseableHttpResponse httpResponse = HTTPClientUtils.getHttpClient(url).execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8.displayName());
				if (StringUtils.isEmpty(result)) {
					return null;
				}
				JSONArray array = JSON.parseArray(result);
				return JSON.parseObject(array.toString(), List.class);
			} else {
				throw new Exception();
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> sendGet(String url) throws Exception {
		try {

			HttpGet httpGet = new HttpGet();

			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
			httpGet.setConfig(requestConfig);

			httpGet.setURI(new URI(url));

			CloseableHttpResponse httpResponse = HTTPClientUtils.getHttpClient(url).execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8.displayName());
				if (StringUtils.isEmpty(result)) {
					return null;
				}
				return JSON.parseArray(result, String.class);
			} else {
				throw new Exception();
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONObject sendGet(String url, Map<String, String> cookieMap) {
		try {
			StringJoiner cookies = new StringJoiner(";");
			for (Entry<String, String> entry : cookieMap.entrySet()) {
				cookies.add(entry.getKey() + "=" + entry.getValue());
			}

			HttpGet httpGet = new HttpGet();

			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeOut).setConnectTimeout(timeOut).setSocketTimeout(timeOut).build();
			httpGet.setConfig(requestConfig);

			httpGet.addHeader(new BasicHeader("Cookie", cookies.toString()));
			httpGet.setURI(new URI(url));

			CloseableHttpResponse httpResponse = getHttpClient(url).execute(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				return JSON.parseObject(EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8.displayName()));

		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static CloseableHttpClient getHttpClient(String url) {
		String hostname = url.split("/")[2];
		int port = 80;
		if (hostname.contains(":")) {
			String[] arr = hostname.split(":");
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}
		if (httpClient == null) {
			synchronized (syncLock) {
				if (httpClient == null) {
					httpClient = createHttpClient(200, 40, 100, hostname, port);
				}
			}
		}
		return httpClient;
	}

	/**
	 * 创建HttpClient对象
	 * 
	 * @return
	 * @author SHANHY
	 * @create 2015年12月18日
	 */
	public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, String hostname, int port) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数添加
		cm.setMaxTotal(maxTotal);
		// 将每一个路由基础的连接添加
		cm.setDefaultMaxPerRoute(maxPerRoute);
		HttpHost httpHost = new HttpHost(hostname, port);
		cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 5) {// 假设已经重试了5次，就放弃
					return false;
				}
				if (exception instanceof NoHttpResponseException) {// 假设server丢掉了连接。那么就重试
					return true;
				}
				if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
					return false;
				}
				if (exception instanceof InterruptedIOException) {// 超时
					return false;
				}
				if (exception instanceof UnknownHostException) {// 目标server不可达
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
					return false;
				}
				if (exception instanceof SSLException) {// SSL握手异常
					return false;
				}

				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				// 假设请求是幂等的，就再次尝试
				if (!(request instanceof HttpEntityEnclosingRequest)) {
					return true;
				}
				return false;
			}
		};
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).build();
		return httpClient;
	}

}
