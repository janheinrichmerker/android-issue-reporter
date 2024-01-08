/*
 * MIT License
 *
 * Copyright (c) 2017 Jan Heinrich Reimer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.heinrichreimersoftware.androidissuereporter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.heinrichreimersoftware.androidissuereporter.databinding.AirActivityIssueReporterBinding;
import com.heinrichreimersoftware.androidissuereporter.databinding.AirCardLoginBinding;
import com.heinrichreimersoftware.androidissuereporter.databinding.AirCardReportBinding;
import com.heinrichreimersoftware.androidissuereporter.model.DeviceInfo;
import com.heinrichreimersoftware.androidissuereporter.model.Report;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubLogin;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;
import com.heinrichreimersoftware.androidissuereporter.util.ColorUtils;
import com.heinrichreimersoftware.androidissuereporter.util.ThemeUtils;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.HttpException;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import static android.util.Patterns.EMAIL_ADDRESS;

public abstract class IssueReporterActivity extends AppCompatActivity {
    private AirActivityIssueReporterBinding binding;
    private AirCardReportBinding reportBinding;
    private AirCardLoginBinding loginBinding;
    private static final String TAG = IssueReporterActivity.class.getSimpleName();

    private static final int STATUS_BAD_CREDENTIALS = 401;
    private static final int STATUS_ISSUES_NOT_ENABLED = 410;
    @StringDef({RESULT_OK, RESULT_BAD_CREDENTIALS, RESULT_INVALID_TOKEN, RESULT_ISSUES_NOT_ENABLED,
            RESULT_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Result {
    }
    private static final String RESULT_OK = "RESULT_OK";
    private static final String RESULT_BAD_CREDENTIALS = "RESULT_BAD_CREDENTIALS";
    private static final String RESULT_INVALID_TOKEN = "RESULT_INVALID_TOKEN";
    private static final String RESULT_ISSUES_NOT_ENABLED = "RESULT_ISSUES_NOT_ENABLED";
    private static final String RESULT_UNKNOWN = "RESULT_UNKNOWN";
    private boolean emailRequired = false;
    private int bodyMinChar = 0;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TextUtils.isEmpty(getTitle()))
            setTitle(R.string.air_title_report_issue);

        binding = AirActivityIssueReporterBinding.inflate(getLayoutInflater());
        reportBinding = binding.airCardReport;
        loginBinding = binding.airCardLogin;
        setContentView(binding.getRoot());

        //noinspection deprecation
        token = getGuestToken();

        initViews();


        DeviceInfo deviceInfo = new DeviceInfo(this);
        reportBinding.airTextDeviceInfo.setText(deviceInfo.toString());
    }

    private void initViews() {
        setSupportActionBar(binding.airToolbar);

        if (NavUtils.getParentActivityName(this) != null) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                binding.airToolbar.setContentInsetsRelative(
                        getResources().getDimensionPixelSize(R.dimen.air_baseline_content),
                        getResources().getDimensionPixelSize(R.dimen.air_baseline));
            }
        }

        reportBinding.airButtonDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.airCardReport.airLayoutDeviceInfo.toggle();
            }
        });


        loginBinding.airInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    reportIssue();
                    return true;
                }
                return false;
            }
        });

        updateGuestTokenViews();

        binding.airButtonSend.setImageResource(ColorUtils.isDark(ThemeUtils.getColorAccent(this)) ?
                R.drawable.air_ic_send_dark : R.drawable.air_ic_send_light);
        binding.airButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportIssue();
            }
        });
    }

    private void setOptionUseAccountMarginStart(int marginStart) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) loginBinding.airOptionUseAccount.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(marginStart);
        } else {
            layoutParams.leftMargin = marginStart;
        }
        loginBinding.airOptionUseAccount.setLayoutParams(layoutParams);
    }

    private void updateGuestTokenViews() {
        if (TextUtils.isEmpty(token)) {
            int baseline = getResources().getDimensionPixelSize(R.dimen.air_baseline);
            int radioButtonPaddingStart = getResources().getDimensionPixelSize(R.dimen.air_radio_button_padding_start);
            setOptionUseAccountMarginStart(-2 * baseline - radioButtonPaddingStart);
            loginBinding.airOptionUseAccount.setEnabled(false);
            loginBinding.airOptionAnonymous.setVisibility(View.GONE);
        } else {
            setOptionUseAccountMarginStart(0);
            loginBinding.airOptionUseAccount.setEnabled(true);
            loginBinding.airOptionUseAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginBinding.airLayoutLogin.expand();
                    loginBinding.airLayoutGuest.collapse();
                    loginBinding.airInputUsername.setEnabled(true);
                    loginBinding.airInputPassword.setEnabled(true);
                }
            });
            loginBinding.airOptionAnonymous.setVisibility(View.VISIBLE);
            loginBinding.airOptionAnonymous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginBinding.airLayoutLogin.collapse();
                    loginBinding.airLayoutGuest.expand();
                    loginBinding.airInputUsername .setEnabled(false);
                    loginBinding.airInputPassword.setEnabled(false);
                }
            });
        }
    }

    private void reportIssue() {

        if (!validateInput()) return;

        if (loginBinding.airOptionUseAccount.isChecked()) {
            String username = loginBinding.airInputUsername.getText().toString();
            String password = loginBinding.airInputPassword.getText().toString();
            sendBugReport(new GithubLogin(username, password), null);
        } else {
            if (TextUtils.isEmpty(token))
                throw new IllegalStateException("You must provide a GitHub API Token.");

            String email = null;
            if (!TextUtils.isEmpty(loginBinding.airInputEmail.getText()) &&
                    EMAIL_ADDRESS.matcher(loginBinding.airInputEmail.getText().toString()).matches()) {
                email = loginBinding.airInputEmail.getText().toString();
            }

            sendBugReport(new GithubLogin(token), email);
        }
    }

    private boolean validateInput() {
        boolean hasErrors = false;

        if (loginBinding.airOptionUseAccount.isChecked()) {
            if (TextUtils.isEmpty(loginBinding.airInputUsername.getText())) {
                setError(loginBinding.airInputUsername, R.string.air_error_no_username);
                hasErrors = true;
            } else {
                removeError(loginBinding.airInputUsername);
            }

            if (TextUtils.isEmpty(loginBinding.airInputPassword.getText())) {
                setError(loginBinding.airInputPassword, R.string.air_error_no_password);
                hasErrors = true;
            } else {
                removeError(loginBinding.airInputPassword);
            }
        } else {
            if (emailRequired) {
                if (TextUtils.isEmpty(loginBinding.airInputEmail.getText()) ||
                        !EMAIL_ADDRESS.matcher(loginBinding.airInputEmail.getText().toString()).matches()) {
                    setError(loginBinding.airInputEmail, R.string.air_error_no_email);
                    hasErrors = true;
                } else {
                    removeError(loginBinding.airInputEmail);
                }
            }
        }

        if (TextUtils.isEmpty(reportBinding.airInputTitle.getText())) {
            setError(reportBinding.airInputTitle, R.string.air_error_no_title);
            hasErrors = true;
        } else {
            removeError(reportBinding.airInputTitle);
        }

        if (TextUtils.isEmpty(reportBinding.airInputDescription.getText())) {
            setError(reportBinding.airInputDescription, R.string.air_error_no_description);
            hasErrors = true;
        } else {
            if (bodyMinChar > 0) {
                if (reportBinding.airInputDescription.getText().toString().length() < bodyMinChar) {
                    setError(reportBinding.airInputDescription, getResources().getQuantityString(R.plurals.air_error_short_description, bodyMinChar, bodyMinChar));
                    hasErrors = true;
                } else {
                    removeError(reportBinding.airInputDescription);
                }
            } else
                removeError(reportBinding.airInputDescription);
        }
        return !hasErrors;
    }

    private void setError(TextInputEditText editText, @StringRes int errorRes) {
        try {
            View layout = (View) editText.getParent();
            while (!layout.getClass().getSimpleName().equals(TextInputLayout.class.getSimpleName()))
                layout = (View) layout.getParent();
            TextInputLayout realLayout = (TextInputLayout) layout;
            realLayout.setError(getString(errorRes));
        } catch (ClassCastException | NullPointerException e) {
            Log.e(TAG, "Issue while setting error UI.", e);
        }
    }

    private void setError(TextInputEditText editText, String error) {
        try {
            View layout = (View) editText.getParent();
            while (!layout.getClass().getSimpleName().equals(TextInputLayout.class.getSimpleName()))
                layout = (View) layout.getParent();
            TextInputLayout realLayout = (TextInputLayout) layout;
            realLayout.setError(error);
        } catch (ClassCastException | NullPointerException e) {
            Log.e(TAG, "Issue while setting error UI.", e);
        }
    }

    private void removeError(TextInputEditText editText) {
        try {
            View layout = (View) editText.getParent();
            while (!layout.getClass().getSimpleName().equals(TextInputLayout.class.getSimpleName()))
                layout = (View) layout.getParent();
            TextInputLayout realLayout = (TextInputLayout) layout;
            realLayout.setError(null);
        } catch (ClassCastException | NullPointerException e) {
            Log.e(TAG, "Issue while removing error UI.", e);
        }
    }

    private void sendBugReport(GithubLogin login, String email) {
        if (!validateInput()) return;

        String bugTitle = reportBinding.airInputTitle.getText().toString();
        String bugDescription = reportBinding.airInputDescription.getText().toString();

        DeviceInfo deviceInfo = new DeviceInfo(this);

        ExtraInfo extraInfo = new ExtraInfo();
        onSaveExtraInfo(extraInfo);

        Report report = new Report(bugTitle, bugDescription, deviceInfo, extraInfo, email);
        GithubTarget target = getTarget();

        ReportIssueTask.report(this, report, target, login);
    }

    protected final void setGuestEmailRequired(boolean required) {
        this.emailRequired = required;
        if (required) {
            loginBinding.airOptionAnonymous.setText(R.string.air_label_use_email);
            loginBinding.airInputEmailParent.setHint(getString(R.string.air_label_email));
        } else {
            loginBinding.airOptionAnonymous.setText(R.string.air_label_use_guest);
            loginBinding.airInputEmailParent.setHint(getString(R.string.air_label_email_optional));
        }
    }

    protected final void setMinimumDescriptionLength(int length) {
        this.bodyMinChar = length;
    }

    protected void onSaveExtraInfo(ExtraInfo extraInfo) {
    }

    protected abstract GithubTarget getTarget();

    @Deprecated
    protected String getGuestToken() {
        return null;
    }

    protected final void setGuestToken(String token) {
        this.token = token;
        Log.d(TAG, "GuestToken: " + token);
        updateGuestTokenViews();
    }

    private static class ReportIssueTask extends DialogAsyncTask<Void, Void, String> {
        private final Report report;
        private final GithubTarget target;
        private final GithubLogin login;

        private ReportIssueTask(Activity activity, Report report, GithubTarget target,
                                GithubLogin login) {
            super(activity);
            this.report = report;
            this.target = target;
            this.login = login;
        }

        private static void report(Activity activity, Report report, GithubTarget target,
                                  GithubLogin login) {
            new ReportIssueTask(activity, report, target, login).execute();
        }

        @Override
        protected Dialog createDialog(@NonNull Context context) {
            return new MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.air_dialog_title_loading)
                    .setView(LayoutInflater.from(context).inflate(R.layout.air_progress_dialog, null))
                    .show();
        }

        @Override
        @Result
        protected String doInBackground(Void... params) {
            GitHub client;
            String repoName = target.getUsername() + "/" + target.getRepository();
            try {
                if (login.shouldUseApiToken()) {
                    client = new GitHubBuilder().withOAuthToken(login.getApiToken()).build();
                } else {
                    client = new GitHubBuilder().withPassword(login.getUsername(), login.getPassword()).build();
                }
                client.getRepository(repoName)
                        .createIssue(report.getTitle())
                        .body(report.getDescription())
                        .create();
                return RESULT_OK;
            } catch (HttpException e) {
                switch (e.getResponseCode()) {
                    case STATUS_BAD_CREDENTIALS:
                        if (login.shouldUseApiToken())
                            return RESULT_INVALID_TOKEN;
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

            Context context = getContext();
            if (context == null) return;

            switch (result) {
                case RESULT_OK:
                    tryToFinishActivity();
                    break;
                case RESULT_BAD_CREDENTIALS:
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.air_dialog_title_failed)
                            .setMessage(R.string.air_dialog_description_failed_wrong_credentials)
                            .setPositiveButton(R.string.air_dialog_action_failed, null)
                            .show();
                    break;
                case RESULT_INVALID_TOKEN:
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.air_dialog_title_failed)
                            .setMessage(R.string.air_dialog_description_failed_invalid_token)
                            .setPositiveButton(R.string.air_dialog_action_failed, null)
                            .show();
                    break;
                case RESULT_ISSUES_NOT_ENABLED:
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.air_dialog_title_failed)
                            .setMessage(R.string.air_dialog_description_failed_issues_not_available)
                            .setPositiveButton(R.string.air_dialog_action_failed, null)
                            .show();
                    break;
                default:
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(R.string.air_dialog_title_failed)
                            .setMessage(R.string.air_dialog_description_failed_unknown)
                            .setPositiveButton(
                                    R.string.air_dialog_action_failed,
                                    (dialog, button) -> tryToFinishActivity())
                            .setOnCancelListener(dialog -> tryToFinishActivity())
                            .show();
                    break;
            }
        }

        private void tryToFinishActivity() {
            Context context = getContext();
            if (context instanceof Activity && !((Activity) context).isFinishing()) {
                ((Activity) context).finish();
            }
        }
    }

    private static abstract class DialogAsyncTask<Pa, Pr, Re> extends AsyncTask<Pa, Pr, Re> {
        private WeakReference<Context> contextWeakReference;
        private WeakReference<Dialog> dialogWeakReference;

        private boolean supposedToBeDismissed;

        private DialogAsyncTask(Context context) {
            contextWeakReference = new WeakReference<>(context);
            dialogWeakReference = new WeakReference<>(null);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Context context = getContext();
            if (!supposedToBeDismissed && context != null) {
                Dialog dialog = createDialog(context);
                dialogWeakReference = new WeakReference<>(dialog);
                dialog.show();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected final void onProgressUpdate(Pr... values) {
            super.onProgressUpdate(values);
            Dialog dialog = getDialog();
            if (dialog != null) {
                onProgressUpdate(dialog, values);
            }
        }

        @SuppressWarnings("unchecked")
        private void onProgressUpdate(@NonNull Dialog dialog, Pr... values) {
        }

        @Nullable
        Context getContext() {
            return contextWeakReference.get();
        }

        @Nullable
        Dialog getDialog() {
            return dialogWeakReference.get();
        }

        @Override
        protected void onCancelled(Re result) {
            super.onCancelled(result);
            tryToDismiss();
        }

        @Override
        protected void onPostExecute(Re result) {
            super.onPostExecute(result);
            tryToDismiss();
        }

        private void tryToDismiss() {
            supposedToBeDismissed = true;
            try {
                Dialog dialog = getDialog();
                if (dialog != null)
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected abstract Dialog createDialog(@NonNull Context context);
    }

}
