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

import java.lang.reflect.Type;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import yao.util.encrypter.CharEncrypter;


/**
 * Created Jansen on 2016/5/21.
 */
public class MethodCallBack<T> extends JsonCallback<T> {
    private INetTasksListener mListener;
    private int taskflag;
    private boolean isEncript;
    private boolean isAddStallId;

    public MethodCallBack(Class<T> clazz, INetTasksListener mListener,
                          boolean isEncript, boolean isAddStallId, int taskflag) {
        super(clazz);
        this.mListener = mListener;
        this.isEncript = isEncript;
        this.taskflag = taskflag;
        this.isAddStallId = isAddStallId;
    }

    public MethodCallBack(Type mType, INetTasksListener mListener,
                          boolean isEncript, boolean isAddStallId, int taskflag) {
        super(mType);
        this.mListener = mListener;
        this.isEncript = isEncript;
        this.taskflag = taskflag;
        this.isAddStallId = isAddStallId;
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        mListener.onTaskStart();
        if (isEncript) {
            String userId = PreferencesUtils.getShareStringData(Constans.USER_ID);
            if (!TextUtils.isEmpty(userId)) {
                sign(userId, request);
            }
        }
    }

    @Override
    public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {
        mListener.onSuccess(t, taskflag);
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        mListener.onFailure(e, "", taskflag);
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable T t, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, t, call, response, e);
        WnLogsUtils.e("是否来自缓存:" + isFromCache);
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        mListener.onLoading(totalSize, currentSize);
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

        if (isAddStallId) {
            request.headers("stall", PreferencesUtils.getShareStringData(Constans.STALL_ID));
        }
        request.headers("time", String.valueOf(new Date().getTime()));
        request.headers("version", "1");
        request.headers("secure", secure);
    }
}
