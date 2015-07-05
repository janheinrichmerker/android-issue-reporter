package com.github.paolorotolo.gitty_reporter_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGittyReporterActivity(View v){
        Intent intent = new Intent(this, GittyReporterExample.class);
        startActivity(intent);
    }

    public void startGittyReporterNoGuestActivity(View v){
        Intent intent = new Intent(this, GittyReporterNoGuestExample.class);
        startActivity(intent);
    }

    public void startGittyReporterNoLoginActivity(View v){
        Intent intent = new Intent(this, GittyReporterNoLoginExample.class);
        startActivity(intent);
    }


}
