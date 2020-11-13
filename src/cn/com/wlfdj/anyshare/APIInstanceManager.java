package cn.com.wlfdj.anyshare;

import io.openDocAPI.client.ApiClient;
import io.openDocAPI.client.Configuration;
import io.openDocAPI.client.api.DefaultApi;

public class APIInstanceManager {

    private DefaultApi apiInstanceWithToken;
    private DefaultApi apiInstanceWithoutToken;
    private TokenManager tokenManager;

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public APIInstanceManager() throws Exception {
        tokenManager = new TokenManager();
        LoginInfo info=new LoginInfo();
        String basePath = "https://" + info.getHost() + ":" + info.getPort() + "/api/v1";
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath(basePath);
        defaultClient.setVerifyingSsl(false);
        defaultClient.setAccessToken(tokenManager.getTokenId());
        apiInstanceWithToken = new DefaultApi(defaultClient);

        ApiClient tmp = Configuration.getDefaultApiClient();
        tmp.setBasePath(basePath);
        tmp.setVerifyingSsl(false);
        apiInstanceWithoutToken = new DefaultApi(tmp);
    }

    /**
     * ����LoginInfo��Ϣ����apiHelper�࣬��ͷ����tokenId����httpsЭ���ȡAPI������ʵ��
     * 
     * @return httpsЭ���ȡAPI������ʵ��
     */
    public DefaultApi getAPIInstanceWithToken() throws Exception {
        apiInstanceWithToken.getApiClient().setAccessToken(tokenManager.getTokenId());
        return apiInstanceWithToken;
    }

    /**
     * ����LoginInfo��Ϣ����apiHelper�࣬��ͷ������tokenId����httpsЭ���ȡAPI������ʵ��
     * 
     * @return httpsЭ���ȡAPI������ʵ��
     */
    public DefaultApi getAPIInstanceWithoutToken() {
        return apiInstanceWithoutToken;
    }

}
