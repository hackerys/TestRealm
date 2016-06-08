package com.wn518.net.https;


import com.wn518.net.utils.WnLogsUtils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created Jansen on 2016/5/6.
 */
public class WNTrustManager implements X509TrustManager {


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        WnLogsUtils.e("ssl证书验证");
        String[] chainBase = new String[]{"CN=*.wn518.com,OU=技术部,O=北京微农科技有限公司,L=beijing,ST=beijing,C=CN",
                "CN=GeoTrust SSL CA - G3,O=GeoTrust Inc.,C=US",
                "CN=GeoTrust Global CA,O=GeoTrust Inc.,C=US"};
        for (int i = 0; i < chain.length; i++) {
            String dName = ((X509Certificate) chain[i]).getSubjectDN().toString();
            dName = dName.replaceAll(",[' ']{1}", ",").trim();
            if (!dName.equals(chainBase[i])) {
                throw new CertificateException("非法证书");
            }
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
