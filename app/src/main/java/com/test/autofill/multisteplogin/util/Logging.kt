package com.test.autofill.multisteplogin.util

import android.util.Log
import com.test.autofill.BuildConfig.DEBUG
import java.io.PrintWriter
import java.io.StringWriter



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

inline fun Any.logW(message: () -> String) {
    if (DEBUG) Log.w(this::class.java.simpleName, message())
}

inline fun Any.logW(e: Throwable, message: () -> String) {
    if (DEBUG) Log.w(this::class.java.simpleName, message(), e)
}

inline fun Any.logE(e: Throwable, message: () -> String) {
    if (DEBUG) Log.e(this::class.java.simpleName, message(), e)
}

inline fun Any.logE(tag: String, e: Throwable, message: () -> String) {
    if (DEBUG) Log.e(tag, message(), e)
}

fun Throwable.printable(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)

    this.printStackTrace(pw)

    return sw.toString()
}