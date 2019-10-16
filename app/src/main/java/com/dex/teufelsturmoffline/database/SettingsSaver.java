package com.dex.teufelsturmoffline.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import com.dex.teufelsturmoffline.R;

public class SettingsSaver {

    private static String AREA = "AREA";

    public static int getArea(FragmentActivity activity){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;
        return sharedPref.getInt(AREA, defaultValue);
    }

    public static void setArea(FragmentActivity activity, int value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(AREA, value);
        editor.commit();
    }
}
