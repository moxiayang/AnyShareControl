package cn.com.wlfdj.anyshare;

import org.apache.log4j.Logger;

import io.openDocAPI.client.ApiException;
import io.openDocAPI.client.api.DefaultApi;
import io.openDocAPI.client.model.FileGetinfobypathReq;

public class AccessAnyShare
{
	private FileUploadDownloadService fileUploadService;
    private APIInstanceManager apiInstceManager;
    private Logger logger;
    public AccessAnyShare() throws Exception 
    {
    	apiInstceManager = new APIInstanceManager();
        fileUploadService = new FileUploadDownloadServiceImpl(apiInstceManager);
        logger=Logger.getLogger("anyshare");
    }
	public void access(String file,String source,String destination) throws Exception
	{
		// TODO Auto-generated method stub
		SingleDownloadReq body = new SingleDownloadReq();
        body.setDocid(source);
        body.setSavePath(destination);
        body.setUsehttps(false);
        try {
            fileUploadService.singleDownload(body);
            logger.info(file+" download completed !");
        } catch (ApiException e) {
            System.out.println(e.getCode() + " " + e.getResponseBody() + " " + e.getResponseHeaders());
            logger.error(file+" download failure ! "+e.getMessage());
        } catch (OSSException e) {
            System.out.println(e.getErrCode() + " " + e.getErrBody() + " " + e.getErrHeaders());
            logger.error(file+" download failure ! "+e.getMessage());
        } catch (Exception e) {
        	logger.error(file+" download failure ! "+e.getMessage());
        }
	}
	public String getDocidByPath(String namePath) throws ApiException {
	        DefaultApi apiInstance = apiInstceManager.getAPIInstanceWithoutToken();
	        FileGetinfobypathReq getinfobypathBody = new FileGetinfobypathReq();
	        getinfobypathBody.setNamepath(namePath);
	        String docId = "";
	        docId = apiInstance.fileGetinfobypathPost(getinfobypathBody).getDocid();
	        return docId;
	    }
}
