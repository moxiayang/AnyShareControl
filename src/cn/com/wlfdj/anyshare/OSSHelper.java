package cn.com.wlfdj.anyshare;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Vector;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class OSSHelper {

    public class OSSReqResult {
        public HttpRequestBase request;
        public CloseableHttpResponse response;
    }

    private CloseableHttpClient ossClient;

    public OSSHelper() throws Exception {
        // �����ƹ���֤�ķ�ʽ����https����
        SSLContext sslcontext = createIgnoreVerifySSL();

        // ����Э��http��https��Ӧ�Ĵ���socket���ӹ����Ķ���
        SSLConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslcontext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", ssl).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        // �����Զ����httpclient����
        ossClient = HttpClients.custom().setConnectionManager(connManager).build();
    }

    public OSSReqResult SendReqToOSS(String method, String url, Vector<String> headers, HttpEntity body)
            throws Exception {
        // set method
        RequestBuilder reqBuilder = RequestBuilder.create(method);
        // set url
        reqBuilder.setUri(url);
        // set body
        if (body != null) {
            reqBuilder.setEntity(body);
        }
        // set headers
        System.out.println(headers);
        for (int i = 0; i < headers.size(); i++) {
            String[] kv = headers.get(i).split(": ", 2);
            reqBuilder.addHeader(kv[0], kv[1]);
        }

        OSSReqResult result = new OSSReqResult();
        result.request = (HttpRequestBase) reqBuilder.build();
        result.response = this.ossClient.execute(result.request);
        int resCode = result.response.getStatusLine().getStatusCode();

        // ��Ϊ���󷵻������׳��쳣
        if (resCode < 200 || resCode >= 300) {
            String errHeaders = "";
            for (int i = 0; i < result.response.getAllHeaders().length; i++) {
                errHeaders += result.response.getAllHeaders()[i].toString() + " ";
            }
            String errBody = EntityUtils.toString(result.response.getEntity(), "utf-8");
            throw new OSSException(resCode, errBody, errHeaders);
        }
        // result.request.releaseConnection();

        return result;
    }

    /**
     * �ƹ���֤
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sc = SSLContext.getInstance("TLS");

        // ʵ��һ��X509TrustManager�ӿڣ������ƹ���֤�������޸�����ķ���
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {}

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {}

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }

}

