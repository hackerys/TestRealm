package com.wn518.net.wn;

import android.app.Application;

import com.wn518.net.OkHttpUtils;
import com.wn518.net.WNHttpEngine;
import com.wn518.net.request.WNFileRequest;
import com.wn518.net.request.WNGetRequest;
import com.wn518.net.request.WNPostRequest;
import com.wn518.net.utils.PreferencesUtils;

/**
 * Created Jansen on 2016/5/16.
 */
public class WNHttpEngineImp extends WNHttpEngine {
    public static final String TAG = "WNHttpEngineImp";
    private static WNHttpEngineImp mInstance;                 //单例
    protected String url;
    protected Object tag;

    public static WNHttpEngineImp getInstance() {
        if (mInstance == null) {
            synchronized (WNHttpEngineImp.class) {
                if (mInstance == null) {
                    mInstance = new WNHttpEngineImp();
                }
            }
        }
        return mInstance;
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    @Override
    public void init(Application app) {
        OkHttpUtils.init(app);
        PreferencesUtils.init(app);
    }

    /**
     * post请求,表单上传
     */
    @Override
    public WNPostRequest post(String url) {
        return new WNPostRequest(url);
    }

    /**
     * get请求
     */
    @Override
    public WNGetRequest get(String url) {
        return new WNGetRequest(url);
    }

    /**
     * get下载
     */
    @Override
    public WNFileRequest download(String url) {
        return new WNFileRequest(url);
    }

    /**
     * 调试模式
     *
     * @param mDebug
     */
    @Override
    public void setDebug(boolean mDebug) {
        if (mDebug) {
            OkHttpUtils.getInstance().debug(TAG);
        }
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    @Override
    public void cancelTag(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}
