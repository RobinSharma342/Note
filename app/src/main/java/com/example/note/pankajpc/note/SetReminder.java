package com.example.note.pankajpc.note;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SetReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button dismissReminder,cancelButton,doneButton;
    TextView dateSelected,timeSelected;
    Spinner selectFrequency;
    Calendar calendar;
    String frequencyType;
    String title,description,timestamp;
    int year,month,date,hourOfDay,minute,priority,requestCodeForDismiss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_reminder);


        title=getIntent().getStringExtra("title");
        setTitle("   "+title);
        description=getIntent().getStringExtra("description");
        timestamp=getIntent().getStringExtra("timestamp");
        priority = getIntent().getIntExtra("priority",0);


        calendar = Calendar.getInstance();
        cancelButton = (Button)findViewById(R.id.cancelButton);
        dismissReminder = (Button)findViewById(R.id.dismissReminder);
        doneButton = (Button)findViewById(R.id.doneButton);
        dateSelected = (TextView) findViewById(R.id.dateSelected);
        timeSelected = (TextView) findViewById(R.id.timeSelected);
        selectFrequency = (Spinner) findViewById(R.id.selectFrequency);

        dateSelected.setOnClickListener(onClickListener);
        timeSelected.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
        doneButton.setOnClickListener(onClickListener);
        dismissReminder.setOnClickListener(onClickListener);
        dismissReminder.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
        requestCodeForDismiss = sharedPreferences.getInt(timestamp,0);
        if(requestCodeForDismiss>0)
        {
            dismissReminder.setVisibility(View.VISIBLE);
        }



        selectFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                frequencyType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.dateSelected:
                    populateDatePicker();
                    break;
                case R.id.timeSelected:
                    populateTimePicker();
                    break;
                case R.id.cancelButton:
                    finish();
                    break;
                case R.id.doneButton:
                    if(dateSelected.getText().toString().equalsIgnoreCase("select date")|| timeSelected.getText().toString().equalsIgnoreCase("select time")){
                        Toast.makeText(SetReminder.this,"Please Select Date/Time",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,date,hourOfDay,minute);
                    Intent intent = new Intent(SetReminder.this,AlarmReceiver.class);
                    ((ApplicationClass)getApplication()).incrementRequestCode();
                    int requestCode = ((ApplicationClass)getApplication()).getRequestCode();
                    intent.putExtra("title",title);
                    intent.putExtra("description",description);
                    intent.putExtra("timestamp",timestamp);
                    intent.putExtra("priority",priority);
                    PendingIntent pendingintent = PendingIntent.getBroadcast(SetReminder.this,requestCode,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    if(frequencyType.equalsIgnoreCase("One Time")){
                        am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingintent);
                    }
                    else if(frequencyType.equalsIgnoreCase("Daily")){
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingintent);

                    }
                    else if(frequencyType.equalsIgnoreCase("Weekly")){
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),604800000L,pendingintent);

                    }
                    else if(frequencyType.equalsIgnoreCase("Monthly")){
                        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),2592000000L,pendingintent);
                    }

                    //saving timestamp with request code
                    SharedPreferences sharedPreferences = getSharedPreferences("mypref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(timestamp,requestCode);
                    editor.apply();
                    finish();
                    break;
                case R.id.dismissReminder:
                    Intent intent1 = new Intent(SetReminder.this,AlarmReceiver.class);
                    PendingIntent pendingintent1 = PendingIntent.getBroadcast(SetReminder.this,requestCodeForDismiss,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager am1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    am1.cancel(pendingintent1);
                    SharedPreferences sharedPreferences1 = getSharedPreferences("mypref",MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.remove(timestamp);
                    editor1.apply();
                    finish();
                    break;
            }

        }
    };

    private void populateDatePicker() {

        DialogFragment datefragment = new DatePickerFragment();
        datefragment.show(getSupportFragmentManager(),"date");


    }
    private void populateTimePicker() {

        DialogFragment timefragment = new TimePickerFragment();
        timefragment.show(getSupportFragmentManager(),"time");


    }
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
        this.year = year;
        this.month = month;
        this.date =date;


        dateSelected.setText(""+month+1+"/"+date+"/"+year);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        this.hourOfDay = hour;
        this.minute = min;
        String amPm;
        String min1 = Integer.toString(min);
        if(min1.equals("0")){
            min1 = "00";
        }
        else if(min1.length()==1){
            min1 = "0"+min1;
        }

        if(hour>12) {
            hour = hour-12;
            amPm = "PM";
        }
        else{
            amPm = "AM";
        }

        timeSelected.setText(""+hour+":"+min1+" "+amPm);
    }
}
