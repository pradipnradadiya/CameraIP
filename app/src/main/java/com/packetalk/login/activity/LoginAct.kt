package com.packetalk.login.activity

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.activity.HomeAct
import com.packetalk.login.model.LoginItem
import com.packetalk.retrofit.APIClient
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.*
import kotlinx.android.synthetic.main.act_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class LoginAct : BaseActivity() {

    override fun getLayoutResourceId(): Int {
        return R.layout.act_login
    }

    override fun init() {

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        tvCopyRight.text =
            "@${Calendar.getInstance().get(Calendar.YEAR)} Packetalk All rights Reserved."
    }

    override fun postInitView() {
    }

    override fun addListener() {
        btnLSubmit.setOnClickListener {
            val valid: Boolean = validateLogin()
            if (valid) {
                //  Check Login
//                startNewActivity(HomeAct::class.java)
//                finish()
                checkLogin(edUserName.text.toString(), edPassword.text.toString())
            }
        }
    }

    override fun loadData() {
    }

    private fun validateLogin(): Boolean {
        if (!Validator.checkEmptyInputText(edUserName, getString(R.string.pls_enter_unm))) {
            return false
        } else if (!Validator.checkEmptyInputText(edPassword, getString(R.string.pls_enter_pwd))) {
            return false
        }
        return true
    }

    private fun checkLogin(emailPhone: String, password: String) {
        showProgressDialog("Login", "Please wait..")
        val loginRaw = HashMap<String, String>()
        loginRaw["username"] = emailPhone
        loginRaw["password"] = password
        val apiInterface = APIClient.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.checkLogin(loginRaw)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                val json = parseJson(response.body().toString())
                val gson = Gson()
                val user = gson.fromJson<LoginItem>(json, LoginItem::class.java)
                val session = SharedPreferenceSession(this@LoginAct)

                if (user.status == AppConstants.STATUS_CODE_SUCCESS) {
                    session.saveUserName(user.userName)
                    session.saveUserType(user.userName)
                    session.saveMemberType(user.memberTypes.toString())
                    session.saveMemberId(user.memberID.toString())
                    startNewActivity(HomeAct::class.java)
                    finish()
                } else {
                    if (user.status != null) {
                        showInfoToast(user.status)
                    } else {
                        showInfoToast("Invalid.")
                    }
                }

                /*  val d = JSONObject(response.body().toString())
                  val json = d.get("d")
                  val obj = JSONObject(json.toString())
                  AppLogger.e(obj.toString())*/


            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                hideProgressDialog()
            }
        })

    }

}
