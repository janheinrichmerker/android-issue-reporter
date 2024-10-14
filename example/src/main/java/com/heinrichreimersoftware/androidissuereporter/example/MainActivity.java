/*
 * MIT License
 *
 * Copyright (c) 2017 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heinrichreimersoftware.androidissuereporter.example;

import android.content.Intent;
import android.os.Bundle;

import com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher;
import com.heinrichreimersoftware.androidissuereporter.example.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        binding.buttonOpenReporter.setOnClickListener(v -> {
            if (binding.optionGuestToken.isChecked()) {
                Intent intent = new Intent(MainActivity.this, ExampleReporterActivity.class);
                startActivity(intent);
            } else {
                IssueReporterLauncher.forTarget("janheinrichmerker", "android-issue-reporter")
                        .theme(R.style.Theme_App)
                        .putExtraInfo("Test 1", "Example string")
                        .putExtraInfo("Test 2", true)
                        .launch(MainActivity.this);
            }
        });
    }
}
