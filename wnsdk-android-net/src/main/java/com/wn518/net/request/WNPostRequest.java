package com.wn518.net.request;

import android.app.Activity;

import com.wn518.net.OkHttpUtils;
import com.wn518.net.cache.CacheMode;
import com.wn518.net.callback.INetTasksListener;
import com.wn518.net.callback.MethodCallBack;

import java.lang.reflect.Type;

/**
 * Created Jansen on 2016/5/21.
 */
public class WNPostRequest extends WNBaseRequest<WNPostRequest> {

    private PostRequest mPostRequest;

    public WNPostRequest(String mUrl) {
        super(mUrl);
        url = mUrl;
        mPostRequest = OkHttpUtils.post(url);
        //默认为304缓存
        mPostRequest.cacheMode(CacheMode.DEFAULT);
        //默认加密
        isEncrypt = true;
    }


    @Override
    public WNPostRequest tag(Object tag) {
        mPostRequest.tag(tag);
        return this;
    }

    @Override
    public WNPostRequest params(String key, String value) {
        mPostRequest.params(key, value);
        return this;
    }

    @Override
    public WNPostRequest headers(String key, String value) {
        mPostRequest.headers(key, value);
        return this;
    }

    @Override
    public WNPostRequest isEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
        return this;
    }

    @Override
    public WNPostRequest isAddStallId(boolean isAddStallId) {
        this.isAddStallId = isAddStallId;
        return this;
    }

    @Override
    public WNPostRequest isCache(boolean isCache) {
        this.isCache = isCache;
        if (isCache) {
            //先使用缓存在请求
            mPostRequest.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST);
        }
        return this;
    }

    @Override
    public WNPostRequest setActivity(Activity mActivity) {
        this.mActivity = mActivity;
        return this;
    }

    @Override
    public WNPostRequest setTaskListener(INetTasksListener mTasksListener) {
        this.mListener = mTasksListener;
        return this;
    }

    /**
     * 类泛型
     *
     * @param mClass
     */
    @Override
    public void execute(Class mClass) {
        if (mClass == null) {
            execute();
            return;
        }
        mPostRequest.execute(new MethodCallBack<>(mClass, mListener, isEncrypt,
                isAddStallId, (int) mPostRequest.tag));
    }

    /**
     * 集合泛型
     *
     * @param mType
     */
    @Override
    public void execute(Type mType) {
        if (mType == null) {
            execute();
            return;
        }
        mPostRequest.execute(new MethodCallBack<>(mType, mListener, isEncrypt,
                isAddStallId, (int) mPostRequest.tag));
    }

    /**
     * json字符串
     */
    @Override
    public void execute() {
        mPostRequest.execute(new MethodCallBack<String>(String.class, mListener, isEncrypt,
                isAddStallId, (int) mPostRequest.tag));
    }
}

