package com.example.note.pankajpc.note;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Pankaj PC on 12-17-2016.
 */

public class NoteModel extends RealmObject {


    @PrimaryKey
    private String mNoteDateTime;
    private String mNoteTitle;
    private String mNoteDescription;
    private int mNotePriority;


    public NoteModel()
    {

    }

    public NoteModel(String mNoteDateTime, String mNoteDescription, String mNoteTitle, int mNotePriority) {
        this.mNoteDateTime = mNoteDateTime;
        this.mNoteDescription = mNoteDescription;
        this.mNoteTitle = mNoteTitle;
        this.mNotePriority = mNotePriority;
    }


    public String getmNoteDateTime() {
        return mNoteDateTime;
    }

    public void setmNoteDateTime(String mNoteDateTime) {
        this.mNoteDateTime = mNoteDateTime;
    }

    public String getmNoteDescription() {
        return mNoteDescription;
    }

    public void setmNoteDescription(String mNoteDescription) {
        this.mNoteDescription = mNoteDescription;
    }

    public String getmNoteTitle() {
        return mNoteTitle;
    }

    public void setmNoteTitle(String mNoteTitle) {
        this.mNoteTitle = mNoteTitle;
    }

    public int getmNotePriority() {
        return mNotePriority;
    }

    public void setmNotePriority(int mNotePriority) {
        this.mNotePriority = mNotePriority;
    }
}
