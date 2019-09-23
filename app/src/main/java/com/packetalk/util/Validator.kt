package com.packetalk.util

import android.util.Log
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.util.regex.Pattern

object Validator {
    fun checkEmpty(edt: EditText, msg: String): Boolean {
        if (edt.text.toString().isEmpty()) {
            edt.error = msg
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }


    fun checkEmptyInputText(edt: TextInputEditText, msg: String): Boolean {
        if (Objects.requireNonNull(edt.getText()).toString().isEmpty()) {
            edt.error = msg
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkEmptyInputEditText(edt: TextInputEditText, til: TextInputLayout, msg: String): Boolean {
        if (Objects.requireNonNull(edt.getText()).toString().isEmpty()) {
//            edt.setError(msg)
            til.error = msg;
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkLimit(edt: EditText): Boolean {
        if (edt.text.toString().length > 50) {
            edt.error = "field required"
            edt.isFocusable = true
            edt.requestFocus()

            return false
        }
        return true
    }

    fun checkDescLimit(edt: EditText): Boolean {
        if (edt.text.toString().length > 300) {
            edt.error = "field required"
            edt.isFocusable = true
            edt.requestFocus()

            return false
        }
        return true
    }

    fun checkPhone(edt: EditText): Boolean {

        val PHONE_PATTERN = "\\d{4}([- ]*)\\d{6}"
        val pattern = Pattern.compile(PHONE_PATTERN)
        val matcher = pattern.matcher(edt.text.toString())
        val flg = matcher.matches()
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            edt.error = "Phone Number is not valid"
            return false
        }
        return true

    }

    fun checkEmail(edt: EditText): Boolean {
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(edt.text.toString())

        val flg = matcher.matches()
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkAlphaNumeric(edt: EditText): Boolean {
        val PATTERN = "[-\\p{Alnum}]+"
        val pattern = Pattern.compile(PATTERN)
        val matcher = pattern.matcher(edt.text.toString())
        val flg = matcher.matches()
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }

        return true
    }

    fun checkOnlyAlpha(edt: EditText): Boolean {
        val PATTERN = "[a-zA-z\\s]*"
        val pattern = Pattern.compile(PATTERN)
        val matcher = pattern.matcher(edt.text.toString())
        val flg = matcher.matches()
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkPin(edt: EditText): Boolean {
        val PATTERN = "^\\(?([0-9]{3})\\)?[-.\\\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$"
        val pattern = Pattern.compile(PATTERN)
        val matcher = pattern.matcher(edt.text.toString())
        val flg = matcher.matches()
        Log.e("---", "-" + matcher.matches())
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkPasswordLength(reg_password: EditText): Boolean {
        val value = reg_password.text.toString()
        val password_length = value.length
        if (password_length < 8) {
            reg_password.error = "Password length is not 8."
            reg_password.isFocusable = true
            reg_password.requestFocus()
            return false
        }
        if (password_length > 25) {
            reg_password.error = "Password length is more 25."
            reg_password.isFocusable = true
            reg_password.requestFocus()
            return false
        }
        return true
    }

    fun checkPassword(edt: EditText): Boolean {
        val PATTERN = "^(?=.*\\d)(?=.*[a-zA-Z]).{8,25}$"
        val pattern = Pattern.compile(PATTERN)
        val matcher = pattern.matcher(edt.text.toString())
        val flg = matcher.matches()
        if (!flg) {
            edt.isFocusable = true
            edt.requestFocus()
            return false
        }
        return true
    }

    fun checkAdult(year: Int, month: Int, day: Int): Boolean {
        val userAge = GregorianCalendar(year, month, day)
        val minAdultAge = GregorianCalendar()
        minAdultAge.add(Calendar.YEAR, -18)
        return if (minAdultAge.before(userAge)) {
            false
        } else true
    }
}
