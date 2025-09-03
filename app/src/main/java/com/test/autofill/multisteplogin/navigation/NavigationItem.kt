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
package com.test.autofill.multisteplogin.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.test.autofill.R

class NavigationItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val mCardView: CardView

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.NavigationItem,
            defStyleAttr, defStyleRes
        )
        val labelText = typedArray.getString(R.styleable.NavigationItem_labelText)
        typedArray.recycle()
        val rootView = LayoutInflater.from(context).inflate(R.layout.navigation_item, this)
        val buttonLabel = rootView.findViewById<TextView>(R.id.buttonLabel)
        buttonLabel.text = labelText

        mCardView = rootView.findViewById<CardView>(R.id.cardView)
    }

    fun setNavigationButtonClickListener(l: OnClickListener?) {
        mCardView.setOnClickListener(l)
    }
}
