package com.wn518.net.request;

import android.app.Activity;

import com.wn518.net.OkHttpUtils;
import com.wn518.net.cache.CacheMode;
import com.wn518.net.callback.DownloadFileCallBack;
import com.wn518.net.callback.INetTasksListener;

import java.lang.reflect.Type;

/**
 * Created Jansen on 2016/5/21.
 */
public class WNFileRequest extends WNBaseRequest<WNFileRequest> {
    private String destFileDir = "";
    private String destFileName = "";
    private GetRequest mGetRequest;

    public WNFileRequest(String mUrl) {
        super(mUrl);
        url = mUrl;
        mGetRequest = OkHttpUtils.get(url);
        //默认为304缓存
        mGetRequest.cacheMode(CacheMode.DEFAULT);
        //默认加密
        isEncrypt = true;
    }


    @Override
    public WNFileRequest tag(Object tag) {
        mGetRequest.tag(tag);
        return this;
    }

    @Override
    public WNFileRequest params(String key, String value) {
        mGetRequest.params(key, value);
        return this;
    }

    @Override

    public WNFileRequest headers(String key, String value) {

        mGetRequest.headers(key, value);

        return this;
    }

    @Override
    public WNFileRequest isEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
        return this;
    }

    @Override
    public WNFileRequest isCache(boolean isCache) {
        this.isCache = isCache;
        if (isCache) {
            //先使用缓存在请求
            mGetRequest.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST);
        }
        return this;
    }

    @Override
    public WNFileRequest isAddStallId(boolean isAddStallId) {
        this.isAddStallId = isAddStallId;
        return this;
    }

    @Override
    public WNFileRequest setActivity(Activity mActivity) {
        this.mActivity = mActivity;
        return this;
    }

    @Override
    public WNFileRequest setTaskListener(INetTasksListener mTasksListener) {
        this.mListener = mTasksListener;
        return this;
    }

    @Override
    public void execute(Class mClass) {

    }

    @Override
    public void execute(Type mType) {

    }

    public WNFileRequest destFileDir(String destFileDir) {
        this.destFileDir = destFileDir;
        return this;
    }

    public WNFileRequest destFileName(String destFileName) {
        this.destFileName = destFileName;
        return this;
    }

    /**
     * json字符串
     */
    @Override
    public void execute() {
        mGetRequest.execute(new DownloadFileCallBack(destFileDir, destFileName, mListener, isEncrypt, (int) mGetRequest.tag));
    }
}

