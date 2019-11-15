package com.packetalk.util

import android.util.Log

object AppLogger {
    private val Any.TAG: String
        get() {
            val tag = javaClass.simpleName
            return if (tag.length <= 23) tag else tag.substring(0, 23)
        }

    private val Any.RESPONSE: String
        get() {
            val response = "response----------"
            return response
        }

    private val Any.ERROR: String
        get() {
            val error = "error----------"
            return error
        }

    fun v(message: String) {
        Log.v(TAG, message)
    }

    fun d(message: String) {
        Log.d(TAG, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }

    fun w(message: String) {
        Log.w(TAG, message)
    }

    fun e(message: String) {
        Log.e(TAG, message)
    }

    fun response(message: String) {
        Log.e(RESPONSE, message)
    }

    fun error(message: String) {
        Log.e(ERROR, message)
    }
}