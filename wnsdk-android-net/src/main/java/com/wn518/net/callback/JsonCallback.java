package com.wn518.net.callback;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends CommonCallback<T> {

    private Class<T> clazz;
    private Type type;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    //该方法是子线程处理，不能做ui相关的工作
    @Override
    public T parseNetworkResponse(Response response) {
        try {
            String responseData = response.body().string();
            if (TextUtils.isEmpty(responseData))
                return null;
            /**
             * 返回全部的json字符串
             */
            if (clazz == String.class) {
                return (T) responseData;
            }
            else {
                JSONObject jsonObject = new JSONObject(responseData);
                final String data = jsonObject.optString("body", "");
                if (clazz != null)
                    return new Gson().fromJson(data, clazz);
                if (type != null)
                    return new Gson().fromJson(data, type);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("OkHttpUtils", "网络IO流读取错误");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("OkHttpUtils", "JSON解析异常");
        }
        return null;
    }
}