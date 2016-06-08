package com.wn518.net;

import android.os.Environment;

import com.wn518.net.callback.INetTasksListener;
import com.wn518.net.config.Constans;
import com.wn518.net.request.WNPostRequest;
import com.wn518.net.utils.WnLogsUtils;
import com.wn518.net.wn.WNHttpEngineImp;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Yummy on 2016/5/18.
 */
public class WNHttpFactory {

    private static WNHttpFactory httpEngine;

    public static WNHttpFactory InstanceNetEngine() {
        if (httpEngine == null) {
            httpEngine = new WNHttpFactory();
        }
        return httpEngine;
    }

    /* http https请求 */
    public void post(final INetTasksListener listener, final Map<String, Object> map,
                     final int taskflag, String url, final boolean isCacheSave,
                     boolean isAddstallId, Boolean isEncryption) {
        WNPostRequest request = WNHttpEngineImp.getInstance().post(url);
        request.tag(taskflag);
        request.isEncrypt(isEncryption);
        request.setTaskListener(listener);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            request.params(String.valueOf(key), String.valueOf(value));
        }
        request.isCache(isCacheSave);
        request.isAddStallId(isAddstallId);
        request.execute();
    }

    /*
    http https请求，传参数类型，响应数据只用处理code=1的情况，自动解析成对象。
     */
    public void post(final INetTasksListener listener, final Map<String, Object> map,
                     final int taskflag, String url, final boolean isCacheSave,
                     boolean isAddstallId, Boolean isEncryption, Class<?> cls) {
        WNPostRequest request = WNHttpEngineImp.getInstance().post(url);
        request.tag(taskflag);
        request.isEncrypt(isEncryption);
        request.setTaskListener(listener);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            request.params(String.valueOf(key), String.valueOf(value));
        }
        request.isCache(isCacheSave);
        request.isAddStallId(isAddstallId);
        request.execute(cls);
    }

    /* 上传文件 */
    public void upFileTask(final INetTasksListener listener, Map<String, Object> map, final int taskflag, String url) throws Exception {
        WNPostRequest request = WNHttpEngineImp.getInstance().post(url);
        request.tag(taskflag);
        request.isEncrypt(true);
        request.setTaskListener(listener);
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("type")) {
//				params.put((String)key,String.valueOf(value));
            } else {
                request.params((String) key, (File) value);
                WnLogsUtils.e("key:" + key);
                WnLogsUtils.e("value:" + value);
            }
        }
        request.execute();
    }

    /* 升级请求 */
    public void postUpgrade(final INetTasksListener listener, final int taskflag, String url, String verId) {
        WNHttpEngineImp.getInstance().post(url)
                .tag(taskflag)
                .params("verId", verId)
                .params("app", "WeiZhang")
                .params("type", "Android")
                .setTaskListener(listener)
                .execute();
    }

    /* 下载请求 */
    public void postDownload(final INetTasksListener listener, String url) {
        WNHttpEngineImp.getInstance().download(url)
                .destFileDir(Environment.getExternalStorageDirectory() + "/wnDownload")
                .setTaskListener(listener)
                .tag(Constans.DOWNLOAD_FLAG)
                .execute();
    }

    /*
    下载请求 文件保存路径和文件保存名称
     */
    public void postDownload(final INetTasksListener listener, String url, String fileDir, String fileName) {
        WNHttpEngineImp.getInstance().download(url)
                .destFileDir(fileDir)
                .destFileName(fileName)
                .setTaskListener(listener)
                .tag(Constans.DOWNLOAD_FLAG)
                .execute();
    }
}
