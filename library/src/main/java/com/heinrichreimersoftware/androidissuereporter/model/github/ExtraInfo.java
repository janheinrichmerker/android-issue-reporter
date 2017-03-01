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
