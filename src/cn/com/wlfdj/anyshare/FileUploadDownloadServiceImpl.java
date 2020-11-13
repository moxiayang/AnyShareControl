package cn.com.wlfdj.anyshare;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import cn.com.wlfdj.anyshare.OSSHelper.OSSReqResult;
import io.openDocAPI.client.api.DefaultApi;
import io.openDocAPI.client.model.FileOsdownloadReq;
import io.openDocAPI.client.model.FileOsdownloadRes;

public class FileUploadDownloadServiceImpl implements FileUploadDownloadService {

    private OSSHelper ossHttpHelper;
    private APIInstanceManager apiInstceManager;
    private long minSize; // �ֿ��ϴ�����С��λ��С��

    public long getMinSize() {
        return minSize;
    }

    public void setMinSize(long minSize) {
        this.minSize = minSize;
    }

    public FileUploadDownloadServiceImpl(APIInstanceManager apiInstceManager) throws Exception {
        ossHttpHelper = new OSSHelper();
        this.apiInstceManager = apiInstceManager;
        minSize = 4 * 1024 * 1024; // Ĭ��Ϊ4M
    }
    @Override
    public void singleDownload(SingleDownloadReq downloadReq) throws Exception {
        DefaultApi apiInstance = apiInstceManager.getAPIInstanceWithToken();

        // ���� osdownload API
        FileOsdownloadReq osdownloadBody = new FileOsdownloadReq();
        osdownloadBody = downloadReq;
        FileOsdownloadRes osdownloadResult = apiInstance.fileOsdownloadPost(osdownloadBody);
        System.out.println(osdownloadResult);

        // ���ݷ��������صĶ���洢���������洢��������
        File saveFile = new File(downloadReq.getSavePath() + "\\" + osdownloadResult.getName());
        if (saveFile.exists()) {
            throw new Exception("����·������ͬ���ļ�������ʧ�ܡ�");
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
        Vector<String> headers = new Vector<String>();
        List<String> authRequestList = osdownloadResult.getAuthrequest();
        for (int i = 2; i < authRequestList.size(); ++i) {
            String header = authRequestList.get(i);
            headers.add(header);
        }
        OSSReqResult ossResult = ossHttpHelper.SendReqToOSS(authRequestList.get(0), authRequestList.get(1), headers, null);
        BufferedInputStream bis = new BufferedInputStream(ossResult.response.getEntity().getContent());
        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = bis.read(bytes)) != -1) {
            bos.write(bytes, 0, len);
        }
        bis.close();
        ossResult.request.releaseConnection();
        bos.close();
    }

}
