package com.heinrichreimersoftware.androidissuereporter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.heinrichreimersoftware.androidissuereporter.model.DeviceInfo;
import com.heinrichreimersoftware.androidissuereporter.model.Report;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubLogin;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;
import com.heinrichreimersoftware.androidissuereporter.util.ColorUtils;
import com.heinrichreimersoftware.androidissuereporter.util.ThemeUtils;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class IssueReporterActivity extends AppCompatActivity {

    private static final int STATUS_BAD_CREDENTIALS = 401;
    private static final int STATUS_ISSUES_NOT_ENABLED = 410;

    @StringDef({RESULT_OK, RESULT_BAD_CREDENTIALS, RESULT_ISSUES_NOT_ENABLED, RESULT_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Result {
    }

    private static final String RESULT_OK = "RESULT_OK";
    private static final String RESULT_BAD_CREDENTIALS = "RESULT_BAD_CREDENTIALS";
    private static final String RESULT_ISSUES_NOT_ENABLED = "RESULT_ISSUES_NOT_ENABLED";
    private static final String RESULT_UNKNOWN = "RESULT_UNKNOWN";

    private Toolbar toolbar;

    private TextInputEditText inputTitle;
    private TextInputEditText inputDescription;
    private TextView textDeviceInfo;
    private ImageButton buttonDeviceInfo;
    private ExpandableRelativeLayout layoutDeviceInfo;

    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;
    private RadioButton optionUseAccount;
    private RadioButton optionAnonymous;
    private ExpandableRelativeLayout layoutLogin;

    private FloatingActionButton buttonSend;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TextUtils.isEmpty(getTitle()))
            setTitle(R.string.air_title_report_issue);

        setContentView(R.layout.air_activity_issue_reporter);
        findViews();

        token = getGuestToken();

        initViews();


        DeviceInfo deviceInfo = new DeviceInfo(this);
        textDeviceInfo.setText(deviceInfo.toString());
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.air_toolbar);

        inputTitle = (TextInputEditText) findViewById(R.id.air_inputTitle);
        inputDescription = (TextInputEditText) findViewById(R.id.air_inputDescription);
        textDeviceInfo = (TextView) findViewById(R.id.air_textDeviceInfo);
        buttonDeviceInfo = (ImageButton) findViewById(R.id.air_buttonDeviceInfo);
        layoutDeviceInfo = (ExpandableRelativeLayout) findViewById(R.id.air_layoutDeviceInfo);

        inputUsername = (TextInputEditText) findViewById(R.id.air_inputUsername);
        inputPassword = (TextInputEditText) findViewById(R.id.air_inputPassword);
        optionUseAccount = (RadioButton) findViewById(R.id.air_optionUseAccount);
        optionAnonymous = (RadioButton) findViewById(R.id.air_optionAnonymous);
        layoutLogin = (ExpandableRelativeLayout) findViewById(R.id.air_layoutLogin);

        buttonSend = (FloatingActionButton) findViewById(R.id.air_buttonSend);
    }

    private void initViews() {
        setSupportActionBar(toolbar);

        buttonDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDeviceInfo.toggle();
            }
        });

        optionUseAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.expand();
                inputUsername.setEnabled(true);
                inputPassword.setEnabled(true);
            }
        });
        optionAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLogin.collapse();
                inputUsername.setEnabled(false);
                inputPassword.setEnabled(false);
            }
        });

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    reportIssue();
                    return true;
                }
                return false;
            }
        });

        if (TextUtils.isEmpty(token)) {
            optionUseAccount.setChecked(true);
            optionAnonymous.setEnabled(false);
        }

        buttonSend.setImageResource(ColorUtils.isDark(ThemeUtils.getColorAccent(this)) ?
                R.drawable.air_ic_send_dark : R.drawable.air_ic_send_light);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportIssue();
            }
        });
    }

    private void reportIssue() {

        if (!validateInput())
            return;

        if (optionUseAccount.isChecked()) {
            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();
            sendBugReport(new GithubLogin(username, password));
        } else {
            if (TextUtils.isEmpty(token))
                throw new IllegalStateException("You must provide a GitHub API Token.");

            sendBugReport(new GithubLogin(token));
        }
    }

    private boolean validateInput() {
        boolean hasErrors = false;

        if (optionUseAccount.isChecked()) {
            if (TextUtils.isEmpty(inputUsername.getText())) {
                setError(inputUsername, R.string.air_error_no_username);
                hasErrors = true;
            } else {
                removeError(inputUsername);
            }

            if (TextUtils.isEmpty(inputPassword.getText())) {
                setError(inputPassword, R.string.air_error_no_password);
                hasErrors = true;
            } else {
                removeError(inputPassword);
            }
        }

        if (TextUtils.isEmpty(inputTitle.getText())) {
            setError(inputTitle, R.string.air_error_no_title);
            hasErrors = true;
        } else {
            removeError(inputTitle);
        }

        if (TextUtils.isEmpty(inputDescription.getText())) {
            setError(inputDescription, R.string.air_error_no_description);
            hasErrors = true;
        } else {
            removeError(inputDescription);
        }

        return !hasErrors;
    }

    private void setError(TextInputEditText editText, @StringRes int errorRes) {
        TextInputLayout layout = (TextInputLayout) editText.getParent();
        layout.setError(getString(errorRes));
    }

    private void removeError(TextInputEditText editText) {
        TextInputLayout layout = (TextInputLayout) editText.getParent();
        layout.setError(null);
    }

    private void sendBugReport(GithubLogin login) {
        if (!validateInput()) return;

        String bugTitle = inputTitle.getText().toString();
        String bugDescription = inputDescription.getText().toString();

        DeviceInfo deviceInfo = new DeviceInfo(this);

        ExtraInfo extraInfo = new ExtraInfo();
        onSaveExtraInfo(extraInfo);

        Report report = new Report(bugTitle, bugDescription, deviceInfo, extraInfo);
        GithubTarget target = getTarget();

        ReportIssueTask.report(this, report, target, login);
    }

    protected void onSaveExtraInfo(ExtraInfo extraInfo) {
    }

    protected abstract GithubTarget getTarget();

    protected String getGuestToken() {
        return null;
    }

    private static class ReportIssueTask extends AsyncTask<Void, Void, String> {
        private Activity activity;

        private Report report;

        private GithubTarget target;

        private GithubLogin login;

        private Dialog progress;

        public static void report(Activity activity, Report report, GithubTarget target,
                                  GithubLogin login) {
            new ReportIssueTask(activity, report, target, login).execute();
        }

        private ReportIssueTask(Activity activity, Report report, GithubTarget target,
                                GithubLogin login) {
            this.activity = activity;
            this.report = report;
            this.target = target;
            this.login = login;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new MaterialDialog.Builder(activity)
                    .progress(true, 0)
                    .title(R.string.air_dialog_title_loading)
                    .show();
        }

        @Override
        @Result
        protected String doInBackground(Void... params) {
            GitHubClient client;
            if (login.shouldUseApiToken()) {
                client = new GitHubClient().setOAuth2Token(login.getApiToken());
            } else {
                client = new GitHubClient().setCredentials(login.getUsername(), login.getPassword());
            }

            Issue issue = new Issue().setTitle(report.getTitle()).setBody(report.getIssueDescriptionMarkdown());
            try {
                new IssueService(client).createIssue(target.getUsername(), target.getRepository(), issue);
                return RESULT_OK;
            } catch (RequestException e) {
                switch (e.getStatus()) {
                    case STATUS_BAD_CREDENTIALS:
                        return RESULT_BAD_CREDENTIALS;
                    case STATUS_ISSUES_NOT_ENABLED:
                        return RESULT_ISSUES_NOT_ENABLED;
                    default:
                        e.printStackTrace();
                        return RESULT_UNKNOWN;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return RESULT_UNKNOWN;
            }
        }

        @Override
        protected void onPostExecute(@Result String result) {
            super.onPostExecute(result);
            if (progress != null)
                progress.dismiss();

            switch (result) {
                case RESULT_OK:
                    activity.finish();
                    break;
                case RESULT_BAD_CREDENTIALS:
                    new MaterialDialog.Builder(activity)
                            .title(R.string.air_dialog_title_failed)
                            .content(login.shouldUseApiToken() ?
                                    R.string.air_dialog_description_failed_wrong_credentials_token :
                                    R.string.air_dialog_description_failed_wrong_credentials)
                            .positiveText(R.string.air_dialog_action_failed_wrong_credentials)
                            .show();
                    break;
                case RESULT_ISSUES_NOT_ENABLED:
                    new MaterialDialog.Builder(activity)
                            .title(R.string.air_dialog_title_failed)
                            .content(R.string.air_dialog_description_failed_issues_not_available)
                            .positiveText(R.string.air_dialog_action_failed_issues_not_available)
                            .show();
                    break;
                default:
                    new MaterialDialog.Builder(activity)
                            .title(R.string.air_dialog_title_failed)
                            .content(R.string.air_dialog_description_failed_unknown)
                            .positiveText(R.string.air_dialog_action_failed_unknown)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog,
                                                    @NonNull DialogAction which) {
                                    activity.finish();
                                }
                            })
                            .cancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    activity.finish();
                                }
                            })
                            .show();
                    break;
            }
        }
    }
}
