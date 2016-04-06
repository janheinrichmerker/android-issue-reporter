package com.heinrichreimersoftware.androidissuereporter.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button buttonOpenReporter = (Button) findViewById(R.id.buttonOpenReporter);
        if (buttonOpenReporter != null) {
            buttonOpenReporter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox optionGuestToken = (CheckBox) findViewById(R.id.optionGuestToken);
                    if (optionGuestToken != null && optionGuestToken.isChecked()) {
                        Intent intent = new Intent(MainActivity.this, ExampleReporterActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, ExampleReporterNoGuestTokenActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }
}
