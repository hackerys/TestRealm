package com.wn518.net.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 轻量存储工具
 * @author LiLong
 * @date 2014-10-24 下午05:47:29
 * @UpdateData 2014-10-24 下午05:47:29 by_
 */
public class PreferencesUtils {

	private static final String SP_NAME = "hlnz_sharedata";
	private static Context context;
	private static SharedPreferences sp;

	/** 会员ID (uid) */
	public final static String USERID = "USERID";

	public static void init(Context initContext) {
		if (sp == null) {
			context = initContext;
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
	}

	public static int getShareIntData(String key) {
		return sp.getInt(key, 0);
	}

	public static void setShareIntData(String key, int value) {
		sp.edit().putInt(key, value).apply();
	}

	public static String getShareStringData(String key) {
		return sp.getString(key, "");
	}

	public static void setShareStringData(String key, String value) {
		sp.edit().putString(key, value).apply();
	}

	public static boolean getShareBooleanData(String key) {
		return sp.getBoolean(key, false);
	}

	public static void setShareBooleanData(String key, boolean value) {
		sp.edit().putBoolean(key, value).apply();
	}

	public static float getShareFloatData(String key) {
		return sp.getFloat(key, 0f);
	}

	public static void setShareFloatData(String key, float value) {
		sp.edit().putFloat(key, value).apply();
	}

	public static long getShareLongData(String key) {
		return sp.getLong(key, 0l);
	}

	public static void setShareLongData(String key, long value) {
		sp.edit().putLong(key, value).apply();
	}

	public static void deleShareData(String key) {
		sp.edit().remove(key).apply();
	}

	public static void clearShareData() {
		sp.edit().clear().apply();
	}
}
