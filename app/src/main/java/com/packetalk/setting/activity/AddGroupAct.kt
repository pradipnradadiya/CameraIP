package com.packetalk.setting.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.GroupAdapter
import com.packetalk.util.*
import kotlinx.android.synthetic.main.act_add_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddGroupAct : BaseActivity() {

    var session: SharedPreferenceSession? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_add_group
    }

    override fun init() {
        bindToolBarBack("Add Group")
    }

    override fun initView() {
        session = SharedPreferenceSession(this@AddGroupAct)
    }

    override fun postInitView() {
    }

    override fun addListener() {
        btnSubmit.setOnClickListener {
            val check = validateGroup()
            if (check) {
                addGroup(edGroupName.text.toString())
            }

        }
    }

    override fun loadData() {
        getGroupList(session?.memberType.toString(), session?.memberId.toString())
    }

    private fun getGroupList(memberType: String, memberId: String) {
        showProgressDialog("", "Please wait..")
        val loginRaw = HashMap<String, String>()
        loginRaw["memberTypes"] = memberType
        loginRaw["MemberID"] = memberId

        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getGroupList(loginRaw)

        callApi!!.enqueue(object : Callback<GroupCameraItem> {

            override fun onResponse(
                call: Call<GroupCameraItem>,
                response: Response<GroupCameraItem>
            ) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()

                if (response.body()!!.responseResult) {
                    val linearLayout = LinearLayoutManager(this@AddGroupAct)
                    recycleViewGroupList.layoutManager = linearLayout
                    val adapter = GroupAdapter(this@AddGroupAct, response.body()!!.groups)
                    recycleViewGroupList.adapter = adapter
                }
            }

            override fun onFailure(call: Call<GroupCameraItem>, t: Throwable) {
                hideProgressDialog()
            }
        })

    }

    private fun addGroup(groupName: String) {
        showProgressDialog("", getString(R.string.please_wait))
        val map = HashMap<String, String>()
        map["Groupname"] = groupName
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.addGroup(map)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                if (response.isSuccessful) {
                    val jsonObject = parseJsonObject(response.body().toString())
                    if (jsonObject.getBoolean("ResponseResult")) {
                        showSuccessToast("Group added successfully")
                        edGroupName.setText("")
                        getGroupList(session?.memberType.toString(), session?.memberId.toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }

    private fun validateGroup(): Boolean {
        if (!Validator.checkEmptyInputText(edGroupName, getString(R.string.pls_enter_gnm))) {
            return false
        }
        return true
    }

}
