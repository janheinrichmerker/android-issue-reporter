package com.heinrichreimersoftware.androidissuereporter.example;

import android.os.Bundle;

import com.heinrichreimersoftware.androidissuereporter.IssueReporterActivity;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;

public class ExampleReporterActivity extends IssueReporterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMinimumDescriptionLength(12);
    }

    @Override
    public GithubTarget getTarget() {
        return new GithubTarget("HeinrichReimer", "android-issue-reporter");
    }

    @Override
    public String getGuestToken() {
        return "28f479f73db97d912611b27579aad7a76ad2baf5";
    }

    @Override
    public void onSaveExtraInfo(ExtraInfo extraInfo) {
        extraInfo.put("Test 1", "Example string");
        extraInfo.put("Test 2", true);
    }
}
