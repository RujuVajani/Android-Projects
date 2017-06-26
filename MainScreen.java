package com.rujuvajani.scheduledownload;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.Time;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //private double startTime , endTime;
    private int hour, minute;
    public Date start_Time, end_Time;
    TextView textView;
    TimePicker timePick;
    EditText startTime , endTime ;
    ToggleButton togglebutton;
    Button setButton;
    BroadcastReceiver downloadReceiver;
    private final static String DOWNLOAD_URL = "http://www.flowermeaning.com/flower-pics/Tulip-7.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        togglebutton = (ToggleButton) findViewById(R.id.toggleButton);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
        Log.i("Display-"," Clock");
        startTime = (EditText)findViewById(R.id.startTime);
        startTime.setInputType(InputType.TYPE_NULL);
        endTime = (EditText)findViewById(R.id.endTime);
        endTime.setInputType(InputType.TYPE_NULL);
        textView =(TextView) findViewById(R.id.textView);
        setButton = (Button) findViewById(R.id.setButton);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //timePick = (TimePicker)findViewById(R.id.timePicker);
        togglebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Toggle-","inside");
                if(togglebutton.isChecked()) {
                    Toast.makeText(MainScreen.this, "ON", Toast.LENGTH_SHORT).show();
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    setButton.setEnabled(true);
                    textView.setText("Stop Scheduler");
                }
                else {
                    Toast.makeText(MainScreen.this, "OFF", Toast.LENGTH_SHORT).show();
                    textView.setText("Start Scheduler");
                }

            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Log.i("Test-"," start time");
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText( ""+selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute,true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }

            });

        endTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Log.i("Test-", " end time");
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainScreen.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTime.setText("" + selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

    }

    @TargetApi(24)
    public void onClick(View view) {
        Log.i("Method", "inside onclick");
        SimpleDateFormat timeF = new SimpleDateFormat("HH:mm");
        Log.i("Time-", "" + timeF);
        try {
            timeF.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
            start_Time = timeF.parse(startTime.getText().toString());
            end_Time = timeF.parse(endTime.getText().toString());
            Log.i("start Time-", "" + start_Time+ " & "+end_Time);
            // IN RECEIVER
            // DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            this.registerReceiver(downloadReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            //this.downloadId = manager.enqueue(request);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,new Intent());
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

