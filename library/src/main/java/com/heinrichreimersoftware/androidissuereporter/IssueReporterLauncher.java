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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_EXTRA_INFO;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_GUEST_EMAIL_REQUIRED;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_GUEST_TOKEN;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_HOME_AS_UP_ENABLED;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_MIN_DESCRIPTION_LENGTH;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_TARGET_REPOSITORY;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_TARGET_USERNAME;
import static com.heinrichreimersoftware.androidissuereporter.IssueReporterLauncher.Activity.EXTRA_THEME;

public class IssueReporterLauncher {
    private static final String TAG = IssueReporterLauncher.class.getSimpleName();

    private final String targetUsername;
    private final String targetRepository;
    @StyleRes
    private int theme = 0;
    private String guestToken = null;
    private boolean guestEmailRequired = false;
    private int minDescriptionLength = 0;
    private ExtraInfo extraInfo = new ExtraInfo();
    private boolean homeAsUpEnabled = true;

    private IssueReporterLauncher(String targetUsername, String targetRepository) {
        this.targetUsername = targetUsername;
        this.targetRepository = targetRepository;
    }

    public static IssueReporterLauncher forTarget(@NonNull String username, @NonNull String repository) {
        return new IssueReporterLauncher(username, repository);
    }

    public static IssueReporterLauncher forTarget(@NonNull GithubTarget target) {
        return new IssueReporterLauncher(target.getUsername(), target.getRepository());
    }

    public IssueReporterLauncher theme(int theme) {
        this.theme = theme;
        return this;
    }

    public IssueReporterLauncher guestToken(String guestToken) {
        this.guestToken = guestToken;
        return this;
    }

    public IssueReporterLauncher guestEmailRequired(boolean guestEmailRequired) {
        this.guestEmailRequired = guestEmailRequired;
        return this;
    }

    public IssueReporterLauncher minDescriptionLength(int minDescriptionLength) {
        this.minDescriptionLength = minDescriptionLength;
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, String value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, boolean value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, double value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, float value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, long value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, int value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(String key, Object value) {
        extraInfo.put(key, value);
        return this;
    }

    public IssueReporterLauncher putExtraInfo(ExtraInfo extraInfo) {
        this.extraInfo.putAll(extraInfo);
        return this;
    }

    public IssueReporterLauncher homeAsUpEnabled(boolean homeAsUpEnabled) {
        this.homeAsUpEnabled = homeAsUpEnabled;
        return this;
    }

    public void launch(Context context) {
        if (theme == 0) {
            Log.w(TAG, "No theme explicitly set for issue reporter activity. " +
                    "Using @style/Theme.IssueReporter implicitly.");
        }
        Intent intent = new Intent(context, Activity.class);
        intent.putExtra(EXTRA_TARGET_USERNAME, targetUsername);
        intent.putExtra(EXTRA_TARGET_REPOSITORY, targetRepository);
        intent.putExtra(EXTRA_THEME, theme);
        intent.putExtra(EXTRA_GUEST_TOKEN, guestToken);
        intent.putExtra(EXTRA_GUEST_EMAIL_REQUIRED, guestEmailRequired);
        intent.putExtra(EXTRA_MIN_DESCRIPTION_LENGTH, minDescriptionLength);
        intent.putExtra(EXTRA_EXTRA_INFO, extraInfo.toBundle());
        intent.putExtra(EXTRA_HOME_AS_UP_ENABLED, homeAsUpEnabled);
        context.startActivity(intent);
    }

    public static class Activity extends IssueReporterActivity {
        public static final String EXTRA_TARGET_USERNAME = "IssueReporterLauncher.Activity.EXTRA_TARGET_USERNAME";
        public static final String EXTRA_TARGET_REPOSITORY = "IssueReporterLauncher.Activity.EXTRA_TARGET_REPOSITORY";
        public static final String EXTRA_THEME = "IssueReporterLauncher.Activity.EXTRA_THEME";
        public static final String EXTRA_GUEST_TOKEN = "IssueReporterLauncher.Activity.EXTRA_GUEST_TOKEN";
        public static final String EXTRA_GUEST_EMAIL_REQUIRED = "IssueReporterLauncher.Activity.EXTRA_GUEST_EMAIL_REQUIRED";
        public static final String EXTRA_MIN_DESCRIPTION_LENGTH = "IssueReporterLauncher.Activity.EXTRA_MIN_DESCRIPTION_LENGTH";
        public static final String EXTRA_EXTRA_INFO = "IssueReporterLauncher.Activity.EXTRA_EXTRA_INFO";
        public static final String EXTRA_HOME_AS_UP_ENABLED = "IssueReporterLauncher.Activity.EXTRA_HOME_AS_UP_ENABLED";

        private String targetUsername;
        private String targetRepository;
        private ExtraInfo extraInfo;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            Intent intent = getIntent();
            if (intent == null) {
                finish();
                return;
            }

            @StyleRes
            int theme = intent.getIntExtra(EXTRA_THEME, 0);
            if (theme != 0) {
                setTheme(theme);
            }

            super.onCreate(savedInstanceState);

            if (!intent.hasExtra(EXTRA_TARGET_USERNAME) ||
                    !intent.hasExtra(EXTRA_TARGET_REPOSITORY)) {
                finish();
                return;
            }
            targetUsername = intent.getStringExtra(EXTRA_TARGET_USERNAME);
            targetRepository = intent.getStringExtra(EXTRA_TARGET_REPOSITORY);

            if (TextUtils.isEmpty(targetUsername) || TextUtils.isEmpty(targetRepository)) {
                finish();
                return;
            }

            String token = intent.getStringExtra(EXTRA_GUEST_TOKEN);
            setGuestToken(TextUtils.isEmpty(token) ? null : token);
            setGuestEmailRequired(intent.getBooleanExtra(EXTRA_GUEST_EMAIL_REQUIRED, false));
            setMinimumDescriptionLength(intent.getIntExtra(EXTRA_MIN_DESCRIPTION_LENGTH, 0));

            if (intent.getBooleanExtra(EXTRA_HOME_AS_UP_ENABLED, true)) {
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    View toolbar = findViewById(R.id.air_toolbar);
                    if (toolbar instanceof Toolbar) {
                        ((Toolbar) toolbar).setContentInsetsRelative(
                                getResources().getDimensionPixelSize(R.dimen.air_baseline_content),
                                getResources().getDimensionPixelSize(R.dimen.air_baseline));
                    }
                }
            }

            extraInfo = ExtraInfo.fromBundle(intent.getBundleExtra(EXTRA_EXTRA_INFO));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onSaveExtraInfo(ExtraInfo extraInfo) {
            super.onSaveExtraInfo(extraInfo);
            if (this.extraInfo != null && !this.extraInfo.isEmpty())  {
                extraInfo.putAll(this.extraInfo);
            }
        }

        @Override
        protected GithubTarget getTarget() {
            return new GithubTarget(targetUsername, targetRepository);
        }
    }
}
