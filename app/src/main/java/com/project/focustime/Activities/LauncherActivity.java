package com.project.focustime.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.project.focustime.R;


public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openApp = new Intent(LauncherActivity.this, MainActivity.class);
                LauncherActivity.this.startActivity(openApp);
                LauncherActivity.this.finish();
            }
        },2000);

    }


}
