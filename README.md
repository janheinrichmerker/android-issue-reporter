
android-issue-reporter
===============

[![JitPack](https://jitpack.io/v/com.heinrichreimersoftware/android-issue-reporter.svg)](https://jitpack.io/#com.heinrichreimersoftware/android-issue-reporter)
[![Build Status](https://travis-ci.org/HeinrichReimer/android-issue-reporter.svg?branch=master)](https://travis-ci.org/HeinrichReimer/android-issue-reporter)
[![Apache License 2.0](https://img.shields.io/github/license/HeinrichReimer/android-issue-reporter.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Based on [Paolo Rotolo](https://github.com/PaoloRotolo)'s [Gitty Reporter](https://github.com/PaoloRotolo/GittyReporter)

Is your Inbox full of bug reports and requests from your users?

*android-issue-reporter* is a new material designed library to report issues from your app directly to GitHub, even without an account.

Dependency
----------

*android-issue-reporter* is available on [**jitpack.io**][J]

**Gradle dependency:**
````gradle
repositories {
    maven { url 'https://jitpack.io' }
}
````
````gradle
dependencies {
    compile 'com.heinrichreimersoftware:android-issue-reporter:-SNAPSHOT'
}
````

Get the latest dependency at [jitpack.io][J].

How to use
-----------
Create a new **Activity that extends GittyReporter**:

```java
public class ExampleReporterActivity extends IssueReporterActivity {
    //Where should the issues go?
    //(http://github.com/username/repository)
    @Override
    public GithubTarget getTarget() {
        return new GithubTarget("HeinrichReimer", "android-issue-reporter");
    }

    //[Optional] Auth token to open issues if users don't have a GitHub account
    //You can register a bot account on GitHub and copy ist OAuth2 token here. 
    @Override
    public String getGuestToken() {
        return "28f479f73db97d912611b27579aad7a76ad2baf5";
    }

    //[Optional] Include other relevant info in the bug report (like custom variables)
    @Override
    public void onSaveExtraInfo(ExtraInfo extraInfo) {
        extraInfo.put("Test 1", "Example string");
        extraInfo.put("Test 2", true);
    }
}
```

[J]: https://jitpack.io/#com.heinrichreimersoftware/android-issue-reporter
