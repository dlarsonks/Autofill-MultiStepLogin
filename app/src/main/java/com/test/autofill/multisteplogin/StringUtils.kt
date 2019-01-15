package com.test.autofill.multisteplogin

/**
 * Created by dlarson on 11/2/17.
 */
object StringUtils {

    fun isBlank(input: String?): Boolean {
        if (input == null || input.isEmpty()) {
            return true
        }

        val len = input.length
        for (i in 0 until len) {
            if (!Character.isWhitespace(input[i])) {
                return false
            }
        }
        return true
    }
}
