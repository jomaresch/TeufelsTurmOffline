package com.dex.teufelsturmoffline.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.dex.teufelsturmoffline.R;

import org.mapsforge.core.model.LatLong;

public class SettingsSaver {

    private final static String AREA = "AREA";
    private final static String ZOOM_LVL = "ZOOM_LVL";
    private final static String CENTER_LAT = "CENTER_LAT";
    private final static String CENTER_LONG = "CENTER_LONG";
    private final static String LICENCE_KEY = "LICENCE_KEY";

    private final static int DEFAULT_ZOOM_LVL = 11;
    private final static float DEFAULT_LAT = 50.9182594f;
    private final static float DEFAULT_LONG = 14.0740389f;

    public static int getArea(FragmentActivity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;
        return sharedPref.getInt(AREA, defaultValue);
    }

    public static void setArea(FragmentActivity activity, int value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(AREA, value);
        editor.commit();
    }

    public static void setZoomLvl(FragmentActivity activity, byte lvl) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(ZOOM_LVL, lvl);
        editor.commit();
    }

    public static byte getZoomLvl(FragmentActivity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return (byte) sharedPref.getInt(ZOOM_LVL, DEFAULT_ZOOM_LVL);
    }

    public static void setCenter(FragmentActivity activity, LatLong center) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(CENTER_LAT, (float) center.latitude);
        editor.putFloat(CENTER_LONG, (float) center.longitude);
        editor.commit();
    }

    public static LatLong getCenter(FragmentActivity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        double lat = sharedPref.getFloat(CENTER_LAT, DEFAULT_LAT);
        double lon = sharedPref.getFloat(CENTER_LONG, DEFAULT_LONG);
        return new LatLong(lat,lon);
    }

    public static String getKey(FragmentActivity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(LICENCE_KEY, "");
    }

    public static void setKey(FragmentActivity activity, String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LICENCE_KEY, key);
        editor.commit();
    }
}
