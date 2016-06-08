package com.wn518.net;

import android.app.Application;

import com.wn518.net.request.WNBaseRequest;
import com.wn518.net.wn.WNHttpEngineImp;

/**
 * Created Jansen on 2016/6/3.
 */
public abstract class WNHttpEngine {

    private static WNHttpEngine mInstance;

    public static WNHttpEngine getInstance() {
        if (mInstance == null) {
            synchronized (WNHttpEngine.class) {
                if (mInstance == null) {
                    /**
                     * 在此切换不同请求库
                     */
                    mInstance = WNHttpEngineImp.getInstance();
                }
            }
        }
        return mInstance;
    }

    /**
     * 必须在全局Application先调用，获取context上下文，否则缓存无法使用
     */
    public abstract void init(Application app);

    /**
     * post请求,表单上传
     */
    public abstract WNBaseRequest post(String url);

    /**
     * get请求
     */
    public abstract WNBaseRequest get(String url);

    /**
     * get下载
     */
    public abstract WNBaseRequest download(String url);

    /**
     * 调试模式
     *
     * @param mDebug
     */
    public abstract void setDebug(boolean mDebug);

    /**
     * 取消请求
     *
     * @param tag
     */
    public abstract void cancelTag(Object tag);
}
