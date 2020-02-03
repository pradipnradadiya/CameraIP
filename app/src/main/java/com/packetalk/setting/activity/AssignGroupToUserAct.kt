package com.packetalk.setting.activity

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager.HORIZONTAL
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.activity.HomeAct.Companion.userId
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.UserGroupSelectedListAdapter
import com.packetalk.setting.adapter.UserListAdapter
import com.packetalk.setting.request.trailor.group.AssignUserToGroupRequest
import com.packetalk.user.model.users.Object
import com.packetalk.user.model.users.UserListItem
import com.packetalk.util.AppConstants.GROUP_ID
import com.packetalk.util.AppConstants.GROUP_NAME
import com.packetalk.util.AppLogger
import com.packetalk.util.parseJsonObject
import com.packetalk.util.showErrorToast
import com.packetalk.util.showSuccessToast
import kotlinx.android.synthetic.main.act_assign_group_to_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class AssignGroupToUserAct : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                for ((index, value) in groupsUserList?.withIndex()!!) {
                    if (value.isNew) {
                        userIdList.add(value.iD.toInt())
                    }
                    if (index == groupsUserList!!.size - 1) {
                        //some code
                        saveAssignUser(userId, userIdList)
                    }

                }

            }
        }
    }

    private var userIdList = ArrayList<Int>()
    var groupId: String? = null
    private var groupName: String? = null
    var layoutManagerUser: LinearLayoutManager? = null
    var layoutManagerUserGroup: LinearLayoutManager? = null
    var adapterUser: UserListAdapter? = null
    var adapterGroupUser: UserGroupSelectedListAdapter? = null
    var groupsUserList: ArrayList<Object>? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_assign_group_to_user
    }

    override fun init() {
        groupId = intent.getStringExtra(GROUP_ID)
        groupName = intent.getStringExtra(GROUP_NAME)
        AppLogger.e("groupname-----$groupName")
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        bindToolBarBack("Assign group to users")
        layoutManagerUser = LinearLayoutManager(this@AssignGroupToUserAct)
        layoutManagerUserGroup = LinearLayoutManager(this@AssignGroupToUserAct)
        layoutManagerUserGroup!!.orientation = HORIZONTAL

        val s = SpannableStringBuilder("Selected Group: ")
            .bold { append(groupName) }
        tvSelectedGroup.text = s
    }

    override fun postInitView() {
        setLay()
    }

    override fun addListener() {
        btnSave.setOnClickListener(this)
    }

    override fun loadData() {
        getUsersList()
        getAssignUsersList(groupId.toString())
    }

    private fun getUsersList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getUserList()

        callApi!!.enqueue(object : Callback<UserListItem> {
            override fun onResponse(call: Call<UserListItem>, response: Response<UserListItem>) {
                AppLogger.e(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        recycleViewUser.layoutManager = layoutManagerUser
                        adapterUser =
                            UserListAdapter(this@AssignGroupToUserAct, response.body()!!.objectX)
                        recycleViewUser.adapter = adapterUser
                    }
                }
            }

            override fun onFailure(call: Call<UserListItem>, t: Throwable) {
                hideProgressDialog()
            }
        })

    }

    private fun getAssignUsersList(groupId: String) {
        val map = HashMap<String, String>()
        map["GroupID"] = groupId
        AppLogger.e("group id $groupId")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getGroupAssignedUsers(map)
        callApi!!.enqueue(object : Callback<UserListItem> {
            override fun onResponse(call: Call<UserListItem>, response: Response<UserListItem>) {
                AppLogger.e(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        groupsUserList = response.body()!!.objectX
                        adapterGroupUser = UserGroupSelectedListAdapter(
                            this@AssignGroupToUserAct,
                            groupsUserList
                        )
                        recycleViewSelectedGroup.adapter = adapterGroupUser
                    }
                }
            }

            override fun onFailure(call: Call<UserListItem>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }

    private fun setLay() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(this@AssignGroupToUserAct)
            //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
            .setChildGravity(Gravity.TOP)
            //whether RecyclerView can scroll. TRUE by default
            .setScrollingEnabled(true)
            //set maximum views count in a particular row
//            .setMaxViewsInRow(2)
            //set gravity resolver where you can determine gravity for item in position.
            //This method have priority over previous one
            .setGravityResolver { Gravity.CENTER }
            //you are able to break row due to your conditions. Row breaker should return true for that views
//            .setRowBreaker { position -> position == 6 || position == 11 || position == 2 }
            //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
            //STRATEGY_FILL_SPACE or STRATEGY_CENTER
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            // whether strategy is applied to last row. FALSE by default
            .withLastRow(true)
            .build()
        recycleViewSelectedGroup.layoutManager = chipsLayoutManager
    }

    fun refreshAdapter(userId: String, groupUserItem: Object) {
        var flag = false
        for ((index, value) in groupsUserList?.withIndex()!!) {
            if (userId == value.iD) {
                showErrorToast("This user already added in group.")
                flag = true
                break
            } else {
                flag = false
            }
        }
        if (!flag) {
            groupUserItem.isNew = true
            groupsUserList!!.add(groupUserItem)
            adapterGroupUser = UserGroupSelectedListAdapter(
                this@AssignGroupToUserAct,
                groupsUserList
            )
            groupsUserList!!.reverse()
            recycleViewSelectedGroup.adapter = adapterGroupUser
        }
    }

    private fun saveAssignUser(groupId: String, userList: ArrayList<Int>) {
        showProgressDialog("Save User", "Please wait..")
        val data = AssignUserToGroupRequest(groupId, userList)
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.saveAssignUserToGroup(data)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.response(response.body().toString())
                hideProgressDialog()
                val json = parseJsonObject(response.body().toString())
                if (json.getBoolean("ResponseResult")) {
                    showSuccessToast("Group successfully assigned to users.")
                    finish()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.error(t.message.toString())
                hideProgressDialog()
            }

        })
    }
}
