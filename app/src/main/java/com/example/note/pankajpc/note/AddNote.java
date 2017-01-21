package com.example.note.pankajpc.note;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    EditText mNoteTitle,mNoteDescription;
    String title,description,time;
    LinearLayout add_note;
    int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Add a Note");
        mNoteTitle = (EditText)findViewById(R.id.noteTitle);
        add_note = (LinearLayout)findViewById(R.id.add_note);
        mNoteDescription = (EditText)findViewById(R.id.noteDescription);
        mNoteDescription.setTextSize(Integer.parseInt(SharedPreferenceCommon.getEditorFontSize()));
        add_note.setBackgroundColor(Color.parseColor(SharedPreferenceCommon.getEditorBackground()));

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_priority_low:
                priority=1;
                break;
            case R.id.note_priority_medium:
                priority=2;
                break;
            case R.id.note_priority_high:
                priority=3;
                break;
            case R.id.note_save:
                title = mNoteTitle.getText().toString();
                description = mNoteDescription.getText().toString();
             //   time = DateFormat.getDateTimeInstance().format(new Date());
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ");
                time = sdf.format(c.getTime());
                if(description.equalsIgnoreCase("")&& title.equalsIgnoreCase("")){
                    Toast.makeText(this,"Empty Note",Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                else{
                    if(title.equalsIgnoreCase("")){
                        title = description;
                    }
                    NoteAdapter.add(time,description,title,priority);
                    Intent i = new Intent(this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                    return true;
                }




            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }

}
