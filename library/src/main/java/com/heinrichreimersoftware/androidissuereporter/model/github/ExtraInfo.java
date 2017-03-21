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

package com.heinrichreimersoftware.androidissuereporter.model.github;

import android.os.Bundle;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtraInfo {
    private final Map<String, String> extraInfo = new LinkedHashMap<>();

    public void put(String key, String value) {
        extraInfo.put(key, value);
    }

    public void put(String key, boolean value) {
        extraInfo.put(key, Boolean.toString(value));
    }

    public void put(String key, double value) {
        extraInfo.put(key, Double.toString(value));
    }

    public void put(String key, float value) {
        extraInfo.put(key, Float.toString(value));
    }

    public void put(String key, long value) {
        extraInfo.put(key, Long.toString(value));
    }

    public void put(String key, int value) {
        extraInfo.put(key, Integer.toString(value));
    }

    public void put(String key, Object value) {
        extraInfo.put(key, String.valueOf(value));
    }

    public void putAll(ExtraInfo extraInfo) {
        this.extraInfo.putAll(extraInfo.extraInfo);
    }

    public void remove(String key) {
        extraInfo.remove(key);
    }

    public boolean isEmpty() {
        return extraInfo.isEmpty();
    }

    public String toMarkdown() {
        if (extraInfo.isEmpty()) return "";

        StringBuilder output = new StringBuilder();
        output.append("Extra info:\n"
                + "---\n"
                + "<table>\n");
        for (String key : extraInfo.keySet()) {
            output.append("<tr><td>")
                    .append(key)
                    .append("</td><td>")
                    .append(extraInfo.get(key))
                    .append("</td></tr>\n");
        }
        output.append("</table>\n");

        return output.toString();
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle(extraInfo.size());
        for (String key : extraInfo.keySet()) {
            bundle.putString(key, extraInfo.get(key));
        }
        return bundle;
    }

    public static ExtraInfo fromBundle(Bundle bundle) {
        ExtraInfo extraInfo = new ExtraInfo();
        if (bundle == null || bundle.isEmpty()) {
            return extraInfo;
        }
        for (String key : bundle.keySet()) {
            extraInfo.put(key, bundle.getString(key));
        }
        return extraInfo;
    }
}
