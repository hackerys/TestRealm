package com.wn518.net.callback;


import com.wn518.net.config.Constans;
import com.wn518.net.request.BaseRequest;
import com.wn518.net.utils.PreferencesUtils;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）
 * 版    本：1.0
 * 创建日期：2016/4/8
 * 描    述：我的Github地址  https://github.com/jeasonlzy0216
 * 修订历史：该类主要用于在所有请求之前添加公共的请求头或请求参数，例如登录授权的 token,使用的设备信息等
 * ================================================
 */
public abstract class CommonCallback<T> extends AbsCallback<T> {
    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //如果账户已经登录，就添加 token 等等
        if (!"".equals(PreferencesUtils.getShareStringData(Constans.TOKEN))) {
            request.headers("token", PreferencesUtils.getShareStringData(Constans.TOKEN));
        }
    }
}
