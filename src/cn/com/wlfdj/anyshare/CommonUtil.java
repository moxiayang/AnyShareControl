package cn.com.wlfdj.anyshare;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class CommonUtil {
    static String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7JL0DcaMUHumSdhxXTxqiABBC\n"
            + "DERhRJIsAPB++zx1INgSEKPGbexDt1ojcNAc0fI+G/yTuQcgH1EW8posgUni0mcT\n"
            + "E6CnjkVbv8ILgCuhy+4eu+2lApDwQPD9Tr6J8k21Ruu2sWV5Z1VRuQFqGm/c5vaT\n" + "OQE5VFOIXPVTaa25mQIDAQAB";

    /**
     * ��pass����RSA����,���ر������ַ���
     * 
     * @param pass
     * @return
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static String RSAEncode(String pass) throws IOException, GeneralSecurityException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(pubKey)));

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeBase64String(cipher.doFinal(pass.getBytes("UTF-8"))).replace("\r\n", "\n");
    }

}
