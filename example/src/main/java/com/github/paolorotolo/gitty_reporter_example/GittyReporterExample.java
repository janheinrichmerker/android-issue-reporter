package com.github.paolorotolo.gitty_reporter_example;

import android.os.Bundle;

import com.github.paolorotolo.gitty_reporter.GittyReporter;

public class GittyReporterExample extends GittyReporter {
    @Override
    public void init(Bundle savedInstanceState) {
        setTargetRepository("paolorotolo", "GittyReporter");
        setGuestOAuth2Token("28f479f73db97d912611b27579aad7a76ad2baf5");
    }
}