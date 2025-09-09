package com.test.autofill.multisteplogin.util

import android.os.Build
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun applyInsetsPaddingIgnoreBottom(viewRoot: View) {
    ViewCompat.setOnApplyWindowInsetsListener(viewRoot) { v: View, insets: WindowInsetsCompat ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.displayCutout())
        v.setPadding(bars.left, bars.top, bars.right, v.paddingBottom)
        insets
    }
}

fun AppCompatActivity.setNavigationBarContrastNotEnforced() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
}

fun ComponentActivity.setNavigationBarContrastNotEnforced() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
    }
}