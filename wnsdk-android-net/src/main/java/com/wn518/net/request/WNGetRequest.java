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
public class WNGetRequest extends WNBaseRequest<WNGetRequest> {

    private GetRequest mGetRequest;

    public WNGetRequest(String mUrl) {
        super(mUrl);
        url = mUrl;
        mGetRequest = OkHttpUtils.get(url);
        //默认为304缓存
        mGetRequest.cacheMode(CacheMode.DEFAULT);
        //默认加密
        isEncrypt = true;
    }


    @Override
    public WNGetRequest tag(Object tag) {
        mGetRequest.tag(tag);
        return this;
    }

    @Override
    public WNGetRequest params(String key, String value) {
        mGetRequest.params(key, value);
        return this;
    }

    @Override
    public WNGetRequest headers(String key, String value) {
        mGetRequest.headers(key, value);
        return this;
    }

    @Override
    public WNGetRequest isEncrypt(boolean isEncrypt) {
        this.isEncrypt = isEncrypt;
        return this;
    }

    @Override
    public WNGetRequest isCache(boolean isCache) {
        this.isCache = isCache;
        if (isCache) {
            //先使用缓存在请求
            mGetRequest.cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST);
        }
        return this;
    }

    @Override
    public WNGetRequest isAddStallId(boolean isAddStallId) {
        this.isAddStallId = isAddStallId;
        return this;
    }

    @Override
    public WNGetRequest setActivity(Activity mActivity) {
        this.mActivity = mActivity;
        return this;
    }

    public WNGetRequest setTaskListener(INetTasksListener mTasksListener) {
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
        mGetRequest.execute(new MethodCallBack<>(mClass, mListener, isEncrypt,
                isAddStallId, (int) mGetRequest.tag));
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
        mGetRequest.execute(new MethodCallBack<>(mType, mListener, isEncrypt,
                isAddStallId, (int) mGetRequest.tag));
    }

    /**
     * json字符串
     */
    @Override
    public void execute() {
        mGetRequest.execute(new MethodCallBack<String>(String.class, mListener, isEncrypt,
                isAddStallId, (int) mGetRequest.tag));
    }
}

