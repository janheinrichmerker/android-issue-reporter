![Icon](http://i.imgur.com/CoPArlm.png)

[![CI status](https://img.shields.io/github/actions/workflow/status/janheinrichmerker/android-issue-reporter/ci.yml?branch=master&style=flat-square)](https://github.com/janheinrichmerker/android-issue-reporter/actions/workflows/ci.yml)
[![JitPack](https://img.shields.io/jitpack/version/com.github.janheinrichmerker/android-issue-reporter?label=JitPack&style=flat-square)](https://jitpack.io/#com.github.janheinrichmerker/android-issue-reporter)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-android--issue--reporter-blue.svg?style=flat-square)](https://android-arsenal.com/details/1/3413)
[![Issues](https://img.shields.io/github/issues/janheinrichmerker/android-issue-reporter?style=flat-square)](https://github.com/janheinrichmerker/android-issue-reporter/issues)
[![Pull requests](https://img.shields.io/github/issues-pr/janheinrichmerker/android-issue-reporter?style=flat-square)](https://github.com/janheinrichmerker/android-issue-reporter/pulls)
[![Commit activity](https://img.shields.io/github/commit-activity/m/janheinrichmerker/android-issue-reporter?style=flat-square)](https://github.com/janheinrichmerker/android-issue-reporter/commits)
[![Maintenance](https://img.shields.io/maintenance/yes/2024?style=flat-square)](https://github.com/janheinrichmerker/android-issue-reporter/graphs/contributors)
[![License](https://img.shields.io/github/license/janheinrichmerker/android-issue-reporter?style=flat-square)](LICENSE)

# android-issue-reporter

Is your mailbox full of bug reports and feature requests from your app users? \
_android-issue-reporter_ is a new material designed library to report issues from your app directly to GitHub, even without an account.

This library is inspired by [Paolo Rotolo](https://github.com/PaoloRotolo)'s [Gitty Reporter](https://github.com/PaoloRotolo/GittyReporter)

## Demo

Download the demo app from the Google Play Store.

<a href="https://play.google.com/store/apps/details?id=com.heinrichreimersoftware.androidissuereporter.example">
	<img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" height="60" />
</a>

## Screenshots

| ![GitHub bot](http://i.imgur.com/ADkPQMo.png) | ![Include device info](http://i.imgur.com/fcFmJ5E.png) | ![Demo](http://i.imgur.com/dJYonBW.png) |
|:-:|:-:|:-:|
| GitHub bot | Include device info | Demo |

## Installation

Add the _android-issue-reporter_ dependency to your app's `build.gradle` (or `build.gradle.kts`):

<details open><summary>Groovy DSL</summary>

```gradle
dependencies {
    implementation 'com.github.janheinrichmerker:android-issue-reporter:1.4.2'
}
```

</details>

<details><summary>Kotlin DSL</summary>

```kotlin
dependencies {
    implementation("com.github.janheinrichmerker:android-issue-reporter:1.4.2")
}
```

</details>

Then, add the JitPack repository in your root `settings.gradle` (or `settings.gradle.kts`):

<details open><summary>Groovy DSL</summary>

```gradle
dependencyResolutionManagement {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

</details>

<details><summary>Kotlin DSL</summary>

```kotlin
dependencyResolutionManagement {
    repositories {
        maven(url = "https://jitpack.io")
    }
}
```

</details>

Check out the latest dependency _android-issue-reporter_ from [jitpack.io](https://jitpack.io/#com.github.janheinrichmerker/android-issue-reporter).

## Usage

### Launch from anywhere with `IssueReporterLauncher`

Just start the issue reporter directly from your activity using the fluent launcher builder:

<details open><summary>Java</summary>

```java
IssueReporterLauncher
        .forTarget("janheinrichmerker", "android-issue-reporter")
        // [Recommended] Theme to use for the reporter. 
        // (See #theming for further information.)
        .theme(R.style.Theme_App_Dark)
        // [Optional] Auth token to open issues if users don't have a GitHub account
        // You can register a bot account on GitHub and copy ist OAuth2 token here.
        // (See #how-to-create-a-bot-key for further information.)
        .guestToken("28f479f73db97d912611b27579aad7a76ad2baf5")
        // [Optional] Force users to enter an email address when the report is sent using
        // the guest token.
        .guestEmailRequired(true)
        // [Optional] Set a minimum character limit for the description to filter out
        // empty reports.
        .minDescriptionLength(20)
        // [Optional] Include other relevant info in the bug report (like custom variables)
        .putExtraInfo("Test 1", "Example string")
        .putExtraInfo("Test 2", true)
        // [Optional] Disable the back arrow in the toolbar
        .homeAsUpEnabled(false)
        .launch(this);
```

</details>

<details><summary>Kotlin</summary>

```kotlin
IssueReporterLauncher
        .forTarget("janheinrichmerker", "android-issue-reporter")
        // [Recommended] Theme to use for the reporter. 
        // (See #theming for further information.)
        .theme(R.style.Theme_App_Dark)
        // [Optional] Auth token to open issues if users don't have a GitHub account
        // You can register a bot account on GitHub and copy ist OAuth2 token here.
        // (See #how-to-create-a-bot-key for further information.)
        .guestToken("28f479f73db97d912611b27579aad7a76ad2baf5")
        // [Optional] Force users to enter an email address when the report is sent using
        // the guest token.
        .guestEmailRequired(true)
        // [Optional] Set a minimum character limit for the description to filter out
        // empty reports.
        .minDescriptionLength(20)
        // [Optional] Include other relevant info in the bug report (like custom variables)
        .putExtraInfo("Test 1", "Example string")
        .putExtraInfo("Test 2", true)
        // [Optional] Disable the back arrow in the toolbar
        .homeAsUpEnabled(false)
        .launch(this)

```

</details>

### Extending `IssueReporterActivity`

Alternatively, if you need to further customize the issue reporter, create a new `Activity` class that extends `IssueReporterActivity`:

<details open><summary>Java</summary>

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
        super.onCreate(savedInstanceState);

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

</details>

<details><summary>Kotlin</summary>

```kotlin
class ExampleReporterActivity : IssueReporterActivity() {
    // Where should the issues go?
    // (http://github.com/username/repository)
    override fun getTarget(): GithubTarget {
        return GithubTarget("username", "repository")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // [Optional] Auth token to open issues if users don't have a GitHub account
        // You can register a bot account on GitHub and copy ist OAuth2 token here.
        // (See #how-to-create-a-bot-key for further information.)
        setGuestToken("28f479f73db97d912611b27579aad7a76ad2baf5")
        
        // [Optional] Force users to enter an email adress when the report is sent using
        // the guest token.
        setGuestEmailRequired(true)
        
        // [Optional] Set a minimum character limit for the description to filter out
        // empty reports.
        setMinimumDescriptionLength(20)
    }

    // [Optional] Include other relevant info in the bug report (like custom variables)
    override fun onSaveExtraInfo(extraInfo: ExtraInfo) {
        extraInfo.put("Test 1", "Example string")
        extraInfo.put("Test 2", true)
    }
}
```

</details>

### Theming
Create a theme extending `Theme.IssueReporter` theme and set it to the launcher using `IssueReporterLauncher.theme(@StyleRes int theme)` or declare it in `AndroidManifest.xml` if you have extended `IssueReporterActivity`:

```xml
<style name="Theme.App" parent="Theme.IssueReporter">
    <item name="colorPrimary">...</item><!-- required -->
    <item name="colorPrimaryDark">...</item><!-- required -->
    <item name="colorAccent">...</item><!-- required -->
</style>
```

### Creating a GitHub bot key

1.  Create a new GitHub account. _(You have to use a unique email address.)_

2.  Go to https://github.com/settings/tokens and create a new token using <kbd>Generate new token</kbd>. _(Only the `public_repo` permission is needed.)_

3.  Copy the OAuth access token you get at the end of the setup.

4.  Use the token in `IssueReporterLauncher.theme(String token)` or override `getGuestToken()` in your reporter activity like this:
    
    <details open><summary>Java</summary>

    ```java
    @Override
    public String getGuestToken() {
        return "<your token here>";
    }
    ```
    
    </details>
    
    <details><summary>Kotlin</summary>
    
    ```kotlin
    override fun getGuestToken(): String {
        return "<your token here>"
    }
    ```
    
    </details>

### Known Limitations
- Two factor authentication is not supported.

## Development

To contribute to the _android-issue-reporter_ library, just [create a codespace](https://codespaces.new/janheinrichmerker/android-issue-reporter) from this repository or clone it in an editor that supports [Dev Containers](https://containers.dev/) (e.g., [Visual Studio Code](https://code.visualstudio.com/learn/develop-cloud/containers) or [IntelliJ](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html)).
All required dependencies will automatically be installed for you. \
Once ready, [create a pull request](https://github.com/janheinrichmerker/android-issue-reporter/compare) with your changes. We're happy to any contribution!

## Support

If you hit any problems using _android-issue-reporter_, please file an [issue](https://github.com/janheinrichmerker/android-issue-reporter/issues). We're happy to help!

## License

This repository is released under the [MIT license](LICENSE).
If you like _android-issue-reporter_, consider [sponsoring me](https://github.com/sponsors/janheinrichmerker).
