
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
public class Gitty extends GittyReporter {

    // Please DO NOT override onCreate. Use init instead.
    @Override
    public void init(Bundle savedInstanceState) {
        
        // Set where Gitty will send issues.
        // (username, repository name);
        setTargetRepository("paolorotolo", "GittyReporter");
        
        // Set Auth token to open issues if user doesn't have a GitHub account
        // For example, you can register a bot account on GitHub that will open bugs for you. 
        setGuestOAuth2Token("28f479f73db97d912611b27579aad7a76ad2baf5");
        
        
        // OPTIONAL METHODS

        // Set if User can send bugs with his own GitHub account (default: true)
        // If false, Gitty will always use your Auth token
        enableUserGitHubLogin(true);
        
        // Set if Gitty can use your Auth token for users without a GitHub account (default: true)
        // If false, Gitty will redirect non registred users to github.com/join
        enableGuestGitHubLogin(true);
        
        // Include other relevant info in your bug report (like custom variables)
        setExtraInfo("Example string");
        
        // Allow users to edit debug info (default: false)
        canEditDebugInfo(true);
        
        // Customize Gitty appearance
        setFabColor1(int colorNormal, int colorPressed, int colorRipple);
        setFabColor2(int colorNormal, int colorPressed, int colorRipple);
        setBackgroundColor1(int color);
        setBackgroundColor2(int color);
        setRippleColor(int color);
    }
}
```

[J]: https://jitpack.io/#com.heinrichreimersoftware/android-issue-reporter
