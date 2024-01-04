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

package com.heinrichreimersoftware.androidissuereporter.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;

public class ThemeUtils {
    @ColorInt
    private static int[] resolveThemeColors(@NonNull Context context, @AttrRes @StyleableRes int[] attrs, @ColorInt int[] defaultColors) {
        if (attrs.length != defaultColors.length)
            throw new IllegalArgumentException("Argument attrs must be the same size as defaultColors");
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, attrs);

        for (int i = 0; i < attrs.length; i++) {
            defaultColors[i] = a.getColor(0, defaultColors[i]);
        }

        a.recycle();

        return defaultColors;
    }

    @ColorInt
    private static int resolveThemeColor(@NonNull Context context, @AttrRes @StyleableRes int attr) {
        return resolveThemeColors(context, new int[]{attr}, new int[]{0})[0];
    }

    @ColorInt
    public static int getColorAccent(@NonNull Context context) {
        return resolveThemeColor(context, com.google.android.material.R.attr.colorSecondary);
    }
}
