![Icon](http://i.imgur.com/CoPArlm.png)

android-issue-reporter
===============

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--issue--reporter-brightgreen.svg?style=flat&v=2)](http://android-arsenal.com/details/1/3413)
[![JitPack](https://jitpack.io/v/com.heinrichreimersoftware/android-issue-reporter.svg)](https://jitpack.io/#com.heinrichreimersoftware/android-issue-reporter)
[![Build Status](https://travis-ci.org/heinrichreimer/android-issue-reporter.svg?branch=master)](https://travis-ci.org/heinrichreimer/android-issue-reporter)
[![License](https://img.shields.io/github/license/heinrichreimer/android-issue-reporter.svg)](https://github.com/heinrichreimer/android-issue-reporter/blob/master/LICENSE.txt)

Based on [Paolo Rotolo](https://github.com/PaoloRotolo)'s [Gitty Reporter](https://github.com/PaoloRotolo/GittyReporter)

Is your Inbox full of bug reports and requests from your users?

*android-issue-reporter* is a new material designed library to report issues from your app directly to GitHub, even without an account.

Demo
----
A demo app is available on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.androidissuereporter.example">
	<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" />
</a>

Screenshots
-----------

| ![material-drawer](http://i.imgur.com/ADkPQMo.png) | ![material-drawer](http://i.imgur.com/fcFmJ5E.png) | ![material-drawer](http://i.imgur.com/dJYonBW.png) |
|:-:|:-:|:-:|
| GitHub bot | Include device info | Demo |

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
    compile 'com.heinrichreimersoftware:android-issue-reporter:1.3.1'
}
````

Get the latest dependency at [jitpack.io][J].

How to use (with `IssueReporterLauncher`)
-----------
Just start the issue reporter directly from your activity using the launcher builder:

```java
IssueReporterLauncher.forTarget("HeinrichReimer", "android-issue-reporter")
        // [Recommended] Theme to use for the reporter. 
        // (See #theming for further information.)
        .theme(R.style.Theme_App_Dark)
        // [Optional] Auth token to open issues if users don't have a GitHub account
        // You can register a bot account on GitHub and copy ist OAuth2 token here.
        // (See #how-to-create-a-bot-key for further information.)
        .guestToken("28f479f73db97d912611b27579aad7a76ad2baf5")
        // [Optional] Force users to enter an email adress when the report is sent using
        // the guest token.
        .guestEmailRequired(true)
        // [Optional] Set a minimum character limit for the description to filter out
        // empty reports.
        .minDescriptionLength(20)
        // [Optional] Include other relevant info in the bug report (like custom variables)
        .putExtraInfo("Test 1", "Example string")
        .putExtraInfo("Test 2", true)
        // [Optional] Disable back arrow in toolbar
        .homeAsUpEnabled(false)
        .launch(this);
```

How to use (extending `IssueReporterActivity`)
-----------
Just create a new `Activity` that extends `IssueReporterActivity`:

```java
public class ExampleReporterActivity extends IssueReporterActivity {
    // Where should the issues go?
    // (http://github.com/username/repository)
    @Override
    public GithubTarget getTarget() {
        return new GithubTarget("username", "repository");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // [Optional] Auth token to open issues if users don't have a GitHub account
        // You can register a bot account on GitHub and copy ist OAuth2 token here.
        // (See #how-to-create-a-bot-key for further information.)
        setGuestToken("28f479f73db97d912611b27579aad7a76ad2baf5")
        
        // [Optional] Force users to enter an email adress when the report is sent using
        // the guest token.
        setGuestEmailRequired(true);
        
        // [Optional] Set a minimum character limit for the description to filter out
        // empty reports.
        setMinimumDescriptionLength(20);
    }

    // [Optional] Include other relevant info in the bug report (like custom variables)
    @Override
    public void onSaveExtraInfo(ExtraInfo extraInfo) {
        extraInfo.put("Test 1", "Example string");
        extraInfo.put("Test 2", true);
    }
}
```

Theming
---
Create a theme extending `Theme.IssueReporter` theme and set it to the launcher using `IssueReporterLauncher.theme(@StyleRes int theme)` or declare it in `AndroidManifest.xml` if you have extended `IssueReporterActivity`:

```xml
<style name="Theme.App.Light" parent="Theme.IssueReporter">
    <item name="colorPrimary">...</item><!-- required -->
    <item name="colorPrimaryDark">...</item><!-- required -->
    <item name="colorAccent">...</item><!-- required -->
</style>
```

You can use `Theme.IssueReporter.Light` or `Theme.IssueReporter.Light.DarkActionBar` as replacement if you want a light theme.

How to create a bot key
---

1.  Create a new GitHub account.  
    _(You have to use a unique email address.)_

2.  Go to https://github.com/settings/tokens and create a new token using <kbd>Generate new token</kbd>.  
    _(You only need to give the bot the `public_repo` permission.)_

3.  Copy the OAuth access token you get at the end of the setup.

4.  Override `getGuestToken()` in your reporter activity like this:
    
    ```java
    @Override
    public String getGuestToken() {
        return "<your token here>";
    }
    ```
    
Limitations
---
- You can't use two factor authentication.

[J]: https://jitpack.io/#com.heinrichreimersoftware/android-issue-reporter
