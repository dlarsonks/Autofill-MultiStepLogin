/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.autofill.multisteplogin.navigation;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.test.autofill.R;

public class NavigationItem extends FrameLayout {
    private final CardView mCardView;

    public NavigationItem(Context context) {
        this(context, null);
    }

    public NavigationItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationItem(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NavigationItem(@NonNull Context context,
                          @Nullable AttributeSet attrs,
                          int defStyleAttr,
                          int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationItem,
                defStyleAttr, defStyleRes);
        String labelText = typedArray.getString(R.styleable.NavigationItem_labelText);
        typedArray.recycle();
        View rootView = LayoutInflater.from(context).inflate(R.layout.navigation_item, this);
        TextView buttonLabel = rootView.findViewById(R.id.buttonLabel);
        buttonLabel.setText(labelText);

        mCardView = rootView.findViewById(R.id.cardView);
    }

    public void setNavigationButtonClickListener(@Nullable OnClickListener l) {
        mCardView.setOnClickListener(l);
    }
}
