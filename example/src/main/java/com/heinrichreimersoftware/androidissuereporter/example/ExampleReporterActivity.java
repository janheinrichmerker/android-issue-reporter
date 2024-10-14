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

package com.heinrichreimersoftware.androidissuereporter.example;

import android.os.Bundle;

import com.heinrichreimersoftware.androidissuereporter.IssueReporterActivity;
import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;
import com.heinrichreimersoftware.androidissuereporter.model.github.GithubTarget;

public class ExampleReporterActivity extends IssueReporterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMinimumDescriptionLength(10);
        setGuestToken("28f479f73db97d912611b27579aad7a76ad2baf5");
    }

    @Override
    public GithubTarget getTarget() {
        return new GithubTarget("janheinrichmerker", "android-issue-reporter");
    }

    @Override
    public void onSaveExtraInfo(ExtraInfo extraInfo) {
        extraInfo.put("Test 1", "Example string");
        extraInfo.put("Test 2", true);
    }
}
