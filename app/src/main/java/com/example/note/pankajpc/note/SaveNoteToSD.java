package com.example.note.pankajpc.note;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Pankaj PC on 01-19-2017.
 */

public class SaveNoteToSD extends AsyncTask<Void,Void,Void> {
    Context context;
    int resultSize;
    private RealmResults<NoteModel> mResults;
    private static Realm mRealm;
    FileOutputStream fos;


    public SaveNoteToSD(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(NoteModel.class).findAll();
        resultSize = mResults.size();
        if(resultSize>0) {
            File noteDirectory = new File(Environment.getExternalStorageDirectory() + "/Notes");
            noteDirectory.mkdirs();

            for (int i = 0; i <mResults.size(); i++) {
                File outputFile = new File(noteDirectory, "" + mResults.get(i).getmNoteTitle() + "_" + mResults.get(i).getmNoteDateTime().substring(0, 6) + ".txt");

                try {
                    fos = new FileOutputStream(outputFile);
                    String noteTitle = mResults.get(i).getmNoteTitle();
                    fos.write(noteTitle.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mRealm.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(resultSize>0) {
            Toast.makeText(context, "Notes Saved to SD", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Note List is empty", Toast.LENGTH_SHORT).show();
        }
    }
}
