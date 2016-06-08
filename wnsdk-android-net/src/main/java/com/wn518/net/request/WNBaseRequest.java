package com.wn518.net.request;

import android.app.Activity;

import com.wn518.net.callback.INetTasksListener;

import java.io.File;
import java.lang.reflect.Type;

/**
 * Created Jansen on 2016/5/25.
 */
public abstract class WNBaseRequest<R extends WNBaseRequest> {
    protected String url;
    protected Object tag;
    protected boolean isCache;
    protected boolean isEncrypt;
    protected boolean isAddStallId;
    protected Activity mActivity;
    protected INetTasksListener mListener;

    public WNBaseRequest(String mUrl) {
        url = mUrl;
    }

    public abstract R tag(Object tag);

    public abstract R params(String key, String value);

    @SuppressWarnings("unchecked")
    public R params(String key, File value) {
        return (R) this;
    }

    public R headers(String key, String value) {
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R destFileDir(String destFileDir) {
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R destFileName(String destFileName) {
        return (R) this;
    }

    public abstract R isEncrypt(boolean isEncrypt);

    public abstract R isCache(boolean isCache);

    public abstract R isAddStallId(boolean isAddStallId);

    public abstract R setActivity(Activity mActivity);

    public abstract R setTaskListener(INetTasksListener mTasksListener);

    public abstract void execute(Class mClass);


    public abstract void execute(Type mType);


    public abstract void execute();
}
