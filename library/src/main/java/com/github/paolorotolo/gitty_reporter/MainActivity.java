package com.github.paolorotolo.gitty_reporter;

public class MainActivity extends GittyReporter {

    @Override
    protected void init() {
        setTargetRepository("paolorotolo", "TestRepo");
        enableGitHubLogin(true);
        setGuestGitHubCredentials("numix-ood", "pa$$w0rd_here");
        setExtraInfo("Example string");
    }
}