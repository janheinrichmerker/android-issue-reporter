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

package com.heinrichreimersoftware.androidissuereporter.model;

import android.text.TextUtils;

import com.heinrichreimersoftware.androidissuereporter.model.github.ExtraInfo;

public class Report {
    private static final String PARAGRAPH_BREAK = "\n\n";
    private static final String HORIZONTAL_RULE = "---";

    private final String title;
    private final String description;
    private final DeviceInfo deviceInfo;
    private final ExtraInfo extraInfo;
    private final String email;

    public Report(String title, String description, DeviceInfo deviceInfo, ExtraInfo extraInfo, String email) {
        this.title = title;
        this.description = description;
        this.deviceInfo = deviceInfo;
        this.extraInfo = extraInfo;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(email)) {
            builder.append("*Sent by [**")
                    .append(email)
                    .append("**](mailto:")
                    .append(email)
                    .append(")*")
                    .append(PARAGRAPH_BREAK);
        }
        builder.append("Description:\n")
                .append(HORIZONTAL_RULE)
                .append(PARAGRAPH_BREAK)
                .append(description)
                .append(PARAGRAPH_BREAK)
                .append(deviceInfo.toMarkdown())
                .append(PARAGRAPH_BREAK)
                .append(extraInfo.toMarkdown());
        return builder.toString();
    }
}
