package com.example.note.pankajpc.note;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Pankaj PC on 01-08-2017.
 */

public class SharedPreferenceCommon {
    static Context context;
    public static SharedPreferences sharedPref;

    public SharedPreferenceCommon(Context context)
    {
        this.context = context;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static String getEditorFontSize(){
        String syncConnPref = sharedPref.getString("pref_font_size", "");
        return syncConnPref;
    }
    public static String getEditorBackground(){
        String syncConnPref = sharedPref.getString("pref_back_color", "");
        return syncConnPref;
    }
    public static String getSortOrder(){
        String syncConnPref = sharedPref.getString("pref_sort_order", "");
        return syncConnPref;
    }

}
