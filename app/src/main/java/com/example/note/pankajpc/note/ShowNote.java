package com.example.note.pankajpc.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShowNote extends AppCompatActivity {
    EditText mnoteTitle,mNoteDescription,mTimeStamp;
    ImageButton mEditButton,mSaveButton, mOptions;
    String title,description,timestamp;
    int priority,changedPriority;
    Realm realm;
    NoteModel nm;
    RelativeLayout mShowNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_note);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Realm.init(this);
        new SharedPreferenceCommon(this);
        realm = Realm.getDefaultInstance();


        mnoteTitle = (EditText) findViewById(R.id.noteTitle);
        mNoteDescription = (EditText) findViewById(R.id.noteDescription);
        mTimeStamp = (EditText)findViewById(R.id.noteDateTime);
        mEditButton = (ImageButton)findViewById(R.id.editButton);
        mSaveButton = (ImageButton)findViewById(R.id.saveButton);
        mOptions    = (ImageButton)findViewById(R.id.options);
        mShowNote   = (RelativeLayout)findViewById(R.id.show_note);
        mNoteDescription.setTextSize(Integer.parseInt(SharedPreferenceCommon.getEditorFontSize()));
        mShowNote.setBackgroundColor(Color.parseColor(SharedPreferenceCommon.getEditorBackground()));


        title=getIntent().getStringExtra("title");
        description=getIntent().getStringExtra("description");
        timestamp=getIntent().getStringExtra("timestamp");
        priority=getIntent().getIntExtra("priority",0);
        changedPriority=priority;



        //get note from database based on what item user clicked
        nm = realm.where(NoteModel.class).equalTo("mNoteDateTime",timestamp).findFirst();
        mnoteTitle.setText(nm.getmNoteTitle());
        mNoteDescription.setText(nm.getmNoteDescription());
        SimpleDateFormat curFormater = new SimpleDateFormat("yyMMddHHmmssZ");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(nm.getmNoteDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String newDateStr = postFormater.format(dateObj);
        mTimeStamp.setText(newDateStr);
        mEditButton.setOnClickListener(editButtonListener);
        mSaveButton.setOnClickListener(editButtonListener);
        mOptions.setOnClickListener(editButtonListener);
       registerForContextMenu(mOptions);
    }

    View.OnClickListener editButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id)
            {
                case R.id.editButton:
                    mnoteTitle.setEnabled(true);
                    mNoteDescription.requestFocus();
                    mNoteDescription.setSelection(mNoteDescription.length());
                    mnoteTitle.setSelection(mnoteTitle.length());
                    mNoteDescription.setEnabled(true);
                    InputMethodManager inputMethodManager =
                            (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    break;
                case R.id.saveButton:
                    onBackPressed();
                    break;
                case R.id.options:
                    PopupMenu popup = new PopupMenu(ShowNote.this, mOptions);
                    popup.getMenuInflater().inflate(R.menu.menu_show_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId())
                            {
                                case R.id.actionLowPriority:
                                    changedPriority=1;
                                    break;
                                case R.id.actionMediumPriority:
                                    changedPriority=2;
                                    break;
                                case R.id.actionHighPriority:
                                    changedPriority=3;
                                    break;
                                case R.id.actionSend:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("text/html");
                                    intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                                    intent.putExtra(Intent.EXTRA_SUBJECT,nm.getmNoteTitle());
                                    intent.putExtra(Intent.EXTRA_TEXT, nm.getmNoteDescription());
                                    ShowNote.this.startActivity(Intent.createChooser(intent, "Send Note"));
                                    break;
                            }
                            return true;
                        }
                    });

                    popup.show();
                    break;

            }
        }
    };

    public void onBackPressed() {
        super.onBackPressed();
        saveNote();


    }

    private void saveNote() {
        String title_temp = mnoteTitle.getText().toString();
        String description_temp = mNoteDescription.getText().toString();
        String time_temp = mTimeStamp.getText().toString();

        if(!(title_temp.equals(title)&& description_temp.equals(description) && priority==changedPriority)) {
            NoteModel model1 = realm.where(NoteModel.class).equalTo("mNoteDateTime", timestamp).findFirst();
            realm.beginTransaction();
            model1.deleteFromRealm();
            realm.commitTransaction();
            realm.close();
            Realm realm;
            realm = Realm.getDefaultInstance();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
            String time1 = sdf.format(c.getTime());
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(new NoteModel(time1, description_temp, title_temp,changedPriority));
            realm.commitTransaction();
            realm.close();
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
    }


}
