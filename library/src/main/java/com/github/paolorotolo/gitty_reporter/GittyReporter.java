package com.github.paolorotolo.gitty_reporter;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import java.io.IOException;

public abstract class GittyReporter extends AppCompatActivity {

    private EditText bugTitleEditText;
    private EditText bugDescriptionEditText;
    private EditText deviceInfoEditText;
    private String deviceInfo;
    private String targetUser;
    private String targetRepository;
    private String gitUser;
    private String gitPassword;
    private Boolean enableGitHubLogin = true;

    @Override
    final protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get Device info and print them in EditText
        deviceInfoEditText = (EditText) findViewById(R.id.device_info);
        getDeviceInfo();
        deviceInfoEditText.setText(deviceInfo);

        init();

        final View nextFab = findViewById(R.id.fab_next);
        final View sendFab = findViewById(R.id.fab_send);

        if (!enableGitHubLogin){
            nextFab.setVisibility(View.INVISIBLE);
            sendFab.setVisibility(View.VISIBLE);
        }
    }

    public void reportIssue (View v) {
        if (enableGitHubLogin) {
            final AppCompatCheckBox githubCheckbox = (AppCompatCheckBox) findViewById(R.id.github_checkbox);
            if (!githubCheckbox.isChecked()){
                EditText userName = (EditText) findViewById(R.id.login_username);
                EditText userPassword = (EditText) findViewById(R.id.login_password);
                this.gitUser = userName.getText().toString();
                this.gitPassword = userPassword.getText().toString();
                sendBugReport();
            } else {
                sendBugReport();
            }
        } else {
            sendBugReport();
        }
    }

    private void sendBugReport(){
        bugTitleEditText = (EditText) findViewById(R.id.bug_title);
        bugDescriptionEditText = (EditText) findViewById(R.id.bug_description);
        final String bugTitle = bugTitleEditText.getText().toString();
        final String bugDescription = bugDescriptionEditText.getText().toString();

        new reportIssue().execute(gitUser, gitPassword, bugTitle, bugDescription, deviceInfo, targetUser, targetRepository);
    }

    public void showLoginPage (View v) {
        // previously invisible view
        final View colorView = findViewById(R.id.material_ripple);
        final View loginView = findViewById(R.id.loginFrame);
        final View nextFab = findViewById(R.id.fab_next);
        final View sendFab = findViewById(R.id.fab_send);

        final AlphaAnimation fadeOutColorAnim = new AlphaAnimation(1.0f, 0.0f);
        fadeOutColorAnim.setDuration(400);
        fadeOutColorAnim.setInterpolator(new AccelerateInterpolator());
        final AlphaAnimation fadeOutFabAnim = new AlphaAnimation(1.0f, 0.0f);
        fadeOutFabAnim.setDuration(400);
        fadeOutFabAnim.setInterpolator(new AccelerateInterpolator());
        final AlphaAnimation fadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnim.setDuration(400);
        fadeInAnim.setInterpolator(new AccelerateInterpolator());

        fadeOutColorAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                loginView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                colorView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOutFabAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendFab.setVisibility(View.VISIBLE);
                sendFab.startAnimation(fadeInAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        int cx = (colorView.getRight());
        int cy = (colorView.getBottom());
        int finalRadius = Math.max(colorView.getWidth(), colorView.getHeight());

        Animator rippleAnim =
                ViewAnimationUtils.createCircularReveal(colorView, cx, cy, 0, finalRadius);

        rippleAnim.setInterpolator(new AccelerateInterpolator());
        rippleAnim.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {
            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {
            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                colorView.startAnimation(fadeOutColorAnim);
                nextFab.startAnimation(fadeOutFabAnim);
                nextFab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {
            }});

        colorView.setVisibility(View.VISIBLE);
        rippleAnim.start();
    }

    public void setTargetRepository(String user, String repository){
        this.targetUser = user;
        this.targetRepository = repository;
    }

    public void setGitHubCredentials(String user, String password){
        this.gitUser = user;
        this.gitPassword = password;
    }

    public void enableGitHubLogin(boolean enableLogin){
        this.enableGitHubLogin = enableLogin;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDeviceInfo() {
        try {
            String s = "Debug-infos:";
            s += "\n OS Version: "      + System.getProperty("os.version")      + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: "    + android.os.Build.VERSION.SDK_INT;
            s += "\n Device: "          + android.os.Build.DEVICE;
            s += "\n Model (and Product): " + android.os.Build.MODEL            + " ("+ android.os.Build.PRODUCT + ")";

            s += "\n RELEASE: "         + android.os.Build.VERSION.RELEASE;
            s += "\n BRAND: "           + android.os.Build.BRAND;
            s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            s += "\n CPU_ABI: "         + android.os.Build.CPU_ABI;
            s += "\n CPU_ABI2: "        + android.os.Build.CPU_ABI2;
            s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            s += "\n Build ID: "        + android.os.Build.ID;
            s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: "          + android.os.Build.SERIAL;
            s += "\n USER: "            + android.os.Build.USER;
            s += "\n HOST: "            + android.os.Build.HOST;

            deviceInfo = s;
        } catch (Exception e) {
            Log.e("android-issue-github", "Error getting Device INFO");
        }
    }

    abstract void init();
}
