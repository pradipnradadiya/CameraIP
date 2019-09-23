package com.packetalk.util

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import com.packetalk.R
import com.packetalk.login.activity.LoginAct


class LogoutDialogClass(private val activity: Activity)// TODO Auto-generated constructor stub
    : Dialog(activity), View.OnClickListener {
    var d: Dialog? = null
    private var yes: Button? = null
    private var no: Button? = null

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_delete)
        yes = findViewById(R.id.btn_yes)
        no = findViewById(R.id.btn_no)
        yes!!.setOnClickListener(this)
        no!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_yes -> logout()
            R.id.btn_no -> dismiss()
            else -> {
            }
        }
        dismiss()
    }

    private fun logout() {
        val session = SharedPreferenceSession(activity)
        session.saveUserType("")
        activity.startActivity(Intent(activity, LoginAct::class.java))
        activity.finishAffinity()
    }

}
