package com.wn518.net.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created Jansen on 2016/6/6.
 */
public class UEDevice {
    /**
     * 获取IMEI
     */
    public static String getDeviceIEMI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }
}
