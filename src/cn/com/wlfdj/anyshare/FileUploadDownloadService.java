package cn.com.wlfdj.anyshare;

/**
 * @author Dylan.gao
 *
 */
public interface FileUploadDownloadService {
    /**
     * �ļ������ϴ�
     * 
     * @param SingleUploadReq
     * @throws Exception
     */
	
    public void singleDownload(SingleDownloadReq downloadReq) throws Exception;
}