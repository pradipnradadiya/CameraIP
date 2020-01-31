package com.packetalk.user.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AddCameraAct
import com.packetalk.user.adapter.AssignCameraAdapter
import com.packetalk.user.adapter.AssignGroupListAdapter
import com.packetalk.user.adapter.ExpandableGroupListAdapter
import com.packetalk.user.model.assign_camera_user.AssignCameraItem
import com.packetalk.user.model.assign_camera_user.PostAssignCamera
import com.packetalk.util.*
import kotlinx.android.synthetic.main.act_assign_camera.*
import kotlinx.android.synthetic.main.dialog_assign_camera_act.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssignCameraAct : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                if (userCameraList.isEmpty()){
                    showWarningToast("No camera to add")
                }else{
                    saveAssignCamera()
                }

            }
            R.id.btnCancel -> {

            }
            R.id.tvAddAllGroupCam -> {
                showDialog()
            }
            R.id.imgClose -> {
                finish()
            }
        }
    }

    lateinit var listAdapter: ExpandableGroupListAdapter
    public lateinit var memberId: String
    private lateinit var memberName: String
    var layoutManager: LinearLayoutManager? = null
    var adapter: AssignCameraAdapter? = null
    lateinit var dialog: Dialog
    var groupList: ArrayList<Groups> = ArrayList()
    var userCameraList: ArrayList<CameraDetailsFull> = ArrayList()
    var flag: Boolean = false


    override fun getLayoutResourceId(): Int {
        return R.layout.act_assign_camera
    }

    override fun init() {
        layoutManager = LinearLayoutManager(this@AssignCameraAct)
        dialog = Dialog(this@AssignCameraAct)
        memberId = intent.getStringExtra(AppConstants.MemberID)
        memberName = intent.getStringExtra(AppConstants.MEMBERNAME)
        AppLogger.e("member id $memberId")
        AppLogger.e("member name $memberName")
    }

    override fun initView() {
        tvUserName.text = memberName
    }

    override fun postInitView() {
    }

    override fun addListener() {
        btnSave.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        tvAddAllGroupCam.setOnClickListener(this)
    }

    override fun loadData() {
        val session = SharedPreferenceSession(this)
        getGroupList(session.memberType.toString(), session.memberId.toString())
        getCamerasForMember(memberId)
    }

    private fun getGroupList(memberType: String, memberId: String) {
        showProgressDialog("Camera", "Please wait..")
        val map = HashMap<String, String>()
        map["memberTypes"] = memberType
        map["MemberID"] = memberId
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getGroupList(map)
        callApi!!.enqueue(object : Callback<GroupCameraItem> {
            override fun onResponse(
                call: Call<GroupCameraItem>,
                response: Response<GroupCameraItem>
            ) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()

                if (response.body()!!.responseResult) {
                    groupList = response.body()!!.groups
                    listAdapter = ExpandableGroupListAdapter(
                        this@AssignCameraAct,
                        response.body()!!.groups

                    )
                    listGroupWithCamera.setAdapter(listAdapter)

                    listGroupWithCamera.setOnGroupClickListener { parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long ->
                        AppLogger.e("group position " + groupPosition.toString())
                        return@setOnGroupClickListener false
                    }

                    listGroupWithCamera.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                        AppLogger.e("child position " + childPosition.toString())

                        val cameraId =
                            groupList[groupPosition].cameraDetailsFull[childPosition].adminCameraIDPK

                        for ((index, value) in userCameraList.withIndex()) {
                            if (cameraId == userCameraList[index].adminCameraIDPK) {
                                showErrorToast("This Camera already added in database.")
                                flag = true
                                break
                            } else {
                                flag = false
                            }
                        }
                        if (!flag) {
                            userCameraList.add(groupList[groupPosition].cameraDetailsFull[childPosition])
                            val position = userCameraList.size - 1
                            userCameraList[position].choice = true
                            adapter = AssignCameraAdapter(this@AssignCameraAct, userCameraList)
                            recycleViewUserCamera.adapter = adapter
                        }
                        return@setOnChildClickListener false
                    }
                }
            }

            override fun onFailure(call: Call<GroupCameraItem>, t: Throwable) {
                hideProgressDialog()
            }
        })

    }

    private fun getCamerasForMember(memberId: String) {
        val map = HashMap<String, String>()
        map["MemberID"] = memberId
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getCamerasForMember(map)
        callApi!!.enqueue(object : Callback<AssignCameraItem> {
            override fun onResponse(
                call: Call<AssignCameraItem>,
                response: Response<AssignCameraItem>
            ) {
                AppLogger.e(response.body().toString())
                if (response.body()!!.responseResult) {
                    recycleViewUserCamera.layoutManager = layoutManager
                    userCameraList = response.body()!!.objectX
                    adapter = AssignCameraAdapter(this@AssignCameraAct, userCameraList)
                    recycleViewUserCamera.adapter = adapter
                }
            }

            override fun onFailure(call: Call<AssignCameraItem>, t: Throwable) {

            }
        })
    }

    private fun saveAssignCamera() {
        showProgressDialog("Camera", "Camera is assigning")
        val gson = Gson()

        /*  val json = gson.toJson(userCameraList)
          AppLogger.e(json)
          val map = HashMap<Any, Any>()
          map["MemberID"] = memberId
          map["UserCameraDetails"] = json
          var datas = JsonArray()
          datas.add(json)
          val req = JsonObject()
          val obj = JSONArray(json.toString())
  //        val obj = JsonArray(json.toString())
          req.addProperty("MemberID", memberId)
          req.addProperty("UserCameraDetails", json)
  */

        val data = PostAssignCamera(userCameraList, memberId)
        val json = gson.toJson(data)
        AppLogger.e("input data " + data)

        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.assignCameraSave(data)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                val json = parseJsonObject(response.body().toString())
                if (json.getBoolean("ResponseResult")) {
                    showSuccessToast("Camera assign successfully")
                    finish()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.e(t.message.toString())
                hideProgressDialog()
            }
        })
    }

    fun removeAt(position: Int) {
//        userCameraList?.removeAt(position)
    }

    private fun showDialog() {
        val camArray: ArrayList<CameraDetailsFull> = ArrayList()
        dialog.setContentView(R.layout.dialog_assign_camera_act)

        val recyclerViewGroup = dialog.findViewById(R.id.recyclerViewGroup) as RecyclerView
        val layoutManager = LinearLayoutManager(this@AssignCameraAct)
//        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewGroup.layoutManager = layoutManager
        val adapter = AssignGroupListAdapter(
            this@AssignCameraAct,
            groupList
        )
        recyclerViewGroup.adapter = adapter

        dialog.btnOk.setOnClickListener {
            checkAndAddCamera(camArray)
            dialog.dismiss()
        }

        dialog.imgClosePop.setOnClickListener {
            dialog.dismiss()
        }


        adapter.onItemClick = { groupData, position ->
            AppLogger.e("position------" + position)
            camArray.clear()
            camArray.addAll(groupData.cameraDetailsFull)
            AppLogger.e("size------" + camArray.size)
        }

        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun checkAndAddCamera(data: ArrayList<CameraDetailsFull>) {

        for ((index, value) in data.withIndex()) {
            value.choice = true
            userCameraList.add(value)
        }

        val set = HashSet(userCameraList)
        userCameraList.clear()
        userCameraList.addAll(set)
        adapter = AssignCameraAdapter(this@AssignCameraAct, userCameraList)
        recycleViewUserCamera.adapter = adapter

    }
}
