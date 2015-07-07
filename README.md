# Gitty Reporter
[![Maven Central](https://img.shields.io/badge/maven--central-gitty--reporter-green.svg
)](http://search.maven.org/#browse%7C-77465355)
<br><a href="https://play.google.com/store/apps/details?id=com.github.paolorotolo.gitty_reporter_example">
  <img alt="Get it on Google Play"
       src="https://developer.android.com/images/brand/en_generic_rgb_wo_45.png" />
</a>

Watch complete video on [YouTube](https://www.youtube.com/watch?v=woYUOilPrpY).

*Is your Inbox full of bug reports and requests from your users?* <br>
**Gitty Reporter is a new material designed library to report issues from your app directly on GitHub, even without an account.**

<img src="https://github.com/PaoloRotolo/GittyReporter/blob/master/art/portrait.png" width="300">
<img src="https://github.com/PaoloRotolo/GittyReporter/blob/master/art/portrait2.png" width="300">
<img src="https://github.com/PaoloRotolo/GittyReporter/blob/master/art/landscape.png" width="600">

## How to use
Add this to your **build.gradle**:
```java
repositories {
    mavenCentral()
}

dependencies {
  compile 'com.github.paolorotolo:gitty_reporter:1.1.1'
}
```

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
        
        // Include other relevant info in your bug report (like custom variables). 
        setExtraInfo("Example string");
    }
}
```

## Featuring Material Design
Gitty Reporter follows Google's material design guidelines.
On >= Lollipop, it also includes material animations [watch them in HD](https://www.youtube.com/watch?v=woYUOilPrpY).

![](https://raw.githubusercontent.com/PaoloRotolo/GittyReporter/master/art/gitty.gif)

## Example
I've uploaded an example app here on GitHub. Check it out! It's also available on [Google Play](https://play.google.com/store/apps/details?id=com.github.paolorotolo.gitty_reporter_example).

## Apps using it
 * Some apps at **Numix Project** Ltd. We created a GitHub bot named [numix-ood](https://github.com/numix-ood) (*Doctor Who anyone?*).
