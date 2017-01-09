package com.example.note.pankajpc.note;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Pankaj PC on 01-07-2017.
 */

public class ApplicationClass extends Application {
    int requestCode;




    public int getRequestCode() {
        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        int request = sharedPreferences.getInt("requestcode",0);
        return request;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
    public void incrementRequestCode() {
        requestCode = getRequestCode();
        requestCode++;
        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("requestcode",requestCode);
        editor.commit();
    }
}
