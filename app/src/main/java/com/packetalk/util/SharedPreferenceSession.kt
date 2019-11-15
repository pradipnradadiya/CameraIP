package com.packetalk.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class SharedPreferenceSession(mContext: Activity) {
    private val sharedPreferences: SharedPreferences
    private val editor: Editor

    val loginData: String?
        get() = sharedPreferences.getString(LOGIN_DATA, "")

    val userName: String?
        get() = sharedPreferences.getString(USERNAME, "")

    val userType: String?
        get() = sharedPreferences.getString(USERTYPE, "")

    val memberType: String?
        get() = sharedPreferences.getString(MEMBERTYPE, "")

    val memberId: String?
        get() = sharedPreferences.getString(MEMBERID, "")

    val password: String?
        get() = sharedPreferences.getString(PASSWORD, "")

    init {
        sharedPreferences = mContext.getSharedPreferences(SHARED, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.apply()
    }

    fun saveLoginData(loginData: String) {
        editor.putString(LOGIN_DATA, loginData)
        editor.commit()
    }

    fun saveUserName(unm: String) {
        editor.putString(USERNAME, unm)
        editor.commit()
    }

    fun saveUserType(uType: String) {
        editor.putString(USERTYPE, uType)
        editor.commit()
    }

    fun saveMemberType(uType: String) {
        editor.putString(MEMBERTYPE, uType)
        editor.commit()
    }

    fun saveMemberId(uType: String) {
        editor.putString(MEMBERID, uType)
        editor.commit()
    }

    fun savePassword(pwd: String) {
        editor.putString(PASSWORD, pwd)
        editor.commit()
    }

    companion object {
        private const val SHARED = "DEMO"
        private const val LOGIN_DATA = "login_data"
        private const val USERNAME = "user_name"
        private const val USERTYPE = "user_type"
        private const val MEMBERTYPE = "member_type"
        private const val MEMBERID = "member_id"
        private const val PASSWORD = "password"
    }
}
