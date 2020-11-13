package cn.com.wlfdj.anyshare;

import io.openDocAPI.client.ApiClient;
import io.openDocAPI.client.Configuration;
import io.openDocAPI.client.api.DefaultApi;
import io.openDocAPI.client.model.Auth1GetnewReq;
import io.openDocAPI.client.model.Auth1GetnewReqDeviceinfo;
import io.openDocAPI.client.model.Auth1GetnewRes;

public class TokenManager {

    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    /**
     * ���캯��
     */
    public TokenManager() throws Exception {

        login();
        // �����Լ���̣߳���֤tokenId��Զ��Ч
        /*long refreshTokenIdTime = 2 * 60 * 60 * 1000 - 10 * 60 * 1000; // tokenIdˢ��ʱ��(��λms) Ĭ��1Сʱ50��;
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(refreshTokenIdTime);
                    login();
                } catch (ApiException e) {
                    System.out.println(e.getCode() + " " + e.getResponseBody() + " " + e.getResponseHeaders());
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    /**
     * ��½
     * 
     * @return tokenid
     */
    public void login() throws Exception {
    	LoginInfo info=new LoginInfo();
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://" + info.getHost() + ":" + info.getPort() + "/api/v1");
        defaultClient.setVerifyingSsl(false);
        DefaultApi apiInstance = new DefaultApi(defaultClient);

        Auth1GetnewReq body = new Auth1GetnewReq();
        body.setAccount(info.getUserName());
        body.setPassword(CommonUtil.RSAEncode(info.getPassword()));
        Auth1GetnewReqDeviceinfo deviceinfo = new Auth1GetnewReqDeviceinfo();
        deviceinfo.setOstype(0L);
        body.setDeviceinfo(deviceinfo);

        Auth1GetnewRes result = apiInstance.auth1GetnewPost(body);
        System.out.println(result);

        this.tokenId = result.getTokenid();
    }
}

