package com.rujuvajani.scheduledownload;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class FlashScreen extends AppCompatActivity {
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final Intent mainIntent = new Intent(FlashScreen.this, MainScreen.class);
                FlashScreen.this.startActivity(mainIntent);
                FlashScreen.this.finish();
            }
        }, 2000);
    }
}
