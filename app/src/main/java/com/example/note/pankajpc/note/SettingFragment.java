package com.example.note.pankajpc.note;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Pankaj PC on 01-08-2017.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
