package com.heinrichreimersoftware.androidissuereporter.example;

import com.heinrichreimersoftware.androidissuereporter.IssueReporterActivity;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;

public class ExampleReporterNoGuestTokenActivity extends IssueReporterActivity {
    @Override
    public GithubTarget getTarget() {
        return new GithubTarget("HeinrichReimer", "android-issue-reporter");
    }

    @Override
    public void onSaveExtraInfo(ExtraInfo extraInfo) {
        extraInfo.put("Test 1", "Example string");
        extraInfo.put("Test 2", true);
    }
}