package com.wn518.net.callback;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.wn518.net.OkHttpUtils;
import com.wn518.net.config.Constans;
import com.wn518.net.encript.AESEncryptSafe;
import com.wn518.net.encript.MD5Util;
import com.wn518.net.request.BaseRequest;
import com.wn518.net.utils.PreferencesUtils;
import com.wn518.net.utils.StringUtils;
import com.wn518.net.utils.UEDevice;
import com.wn518.net.utils.WnLogsUtils;

import java.io.File;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import yao.util.encrypter.CharEncrypter;


/**
 * Created Jansen on 2016/5/25.
 */
public class DownloadFileCallBack extends FileCallback {
    private INetTasksListener mListener;
    private int taskflag;
    private boolean isEncrypt;

    public DownloadFileCallBack(String destFileDir, String destFileName, INetTasksListener mListener,
                                boolean isEncrypt, int taskflag) {
        super(destFileDir, destFileName);
        this.mListener = mListener;
        this.isEncrypt = isEncrypt;
        this.taskflag = taskflag;
    }

    @Override
    public void onBefore(BaseRequest request) {
        mListener.onTaskStart();
        if (isEncrypt) {
            String userId = PreferencesUtils.getShareStringData(Constans.USER_ID);
            if (!TextUtils.isEmpty(userId)) {
                sign(userId, request);
            }
        }
    }

    @Override
    public void onResponse(boolean isFromCache, File file, Request request, Response response) {
        mListener.onSuccess("下载成功", taskflag);
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        WnLogsUtils.e("正在下载");
        mListener.onLoading(totalSize, currentSize);
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        mListener.onFailure(e, "", taskflag);
    }

    private void sign(String userId, BaseRequest request) {
        String randomStr = StringUtils.getRandomString(8);
        CharEncrypter charEncrypter = new CharEncrypter(
                CharEncrypter.S_0to9 + CharEncrypter.S_a2z
                        + CharEncrypter.S_A2Z + "_-+=*@!$", "wnshg");

        String device_code = "";
        if (null != UEDevice.getDeviceIEMI(OkHttpUtils.getContext())) {
            device_code = UEDevice.getDeviceIEMI(OkHttpUtils.getContext());
        }

        String md5 = MD5Util.MD5_32(userId + randomStr + device_code);

        String aaa = new AESEncryptSafe(device_code).encrypt(randomStr);

        String bbb = md5 + aaa;

        String secure = charEncrypter.encrypt(bbb);
        request.headers("time", String.valueOf(new Date().getTime()));
        request.headers("version", "1");
        request.headers("secure", secure);
    }
}
