package com.test.autofill.multisteplogin

/**
 * Created by dlarson on 11/2/17.
 */
fun Any?.notNull(f: () -> Unit) {
    if (this != null) {
        f()
    }
}