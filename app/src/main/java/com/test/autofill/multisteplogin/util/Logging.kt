package com.test.autofill.multisteplogin.util

import android.util.Log
import com.test.autofill.BuildConfig.DEBUG

/**
 * Created by dlarson on 6/18/24
 */

inline fun Any.logD(message: () -> String) {
    if (DEBUG) Log.d(this::class.java.simpleName, message())
}

inline fun Any.logE(message: () -> String) {
    if (DEBUG) Log.e(this::class.java.simpleName, message())
}

inline fun logD(tag: String, message: () -> String) {
    if (DEBUG) Log.d(tag, message())
}
