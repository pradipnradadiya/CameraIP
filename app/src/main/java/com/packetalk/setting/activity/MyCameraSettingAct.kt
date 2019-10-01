package com.packetalk.setting.activity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.MyCameraSettingAdapter
import com.packetalk.setting.request.trailor.my_camera_setting.MyCameraSettingDeleteRequest
import com.packetalk.setting.request.trailor.my_camera_setting.MyCameraSettingPriorityRequest
import com.packetalk.util.*
import kotlinx.android.synthetic.main.act_my_camera_sertting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyCameraSettingAct : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDeleteGroup -> {
                IOSDialog.Builder(this@MyCameraSettingAct)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_msg))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        deleteGroup(groupId)
                    }
                    .setNegativeButton(
                        getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()


            }
            R.id.btnSave -> {
                if (!adapter?.itemArrayList.isNullOrEmpty()) {
                    val arrayCameraIDPK = ArrayList<Int>()
                    val arrayPriority = ArrayList<Int>()
                    val length = adapter?.itemArrayList?.size
                    var flag = false
                    for (i in 0 until length!!) {
                        var j = i + 1
                        while (j < length) {
                            if (adapter?.itemArrayList?.get(i)?.priority?.equals(
                                    adapter?.itemArrayList?.get(
                                        j
                                    )!!.priority
                                )!!
                            ) {
                                showErrorToast("Repeated priority ${i + 1},${j + 1} It should be unique for each camera.")
                                flag = true
                            }
                            j++
                        }
                        if (flag) {
                            break
                        } else {
                            arrayCameraIDPK.add(adapter?.itemArrayList?.get(i)?.adminCameraIDPK!!)
                            arrayPriority.add(adapter?.itemArrayList?.get(i)?.priority!!)
                        }
                    }

                    if (!flag) {
                        val data = MyCameraSettingPriorityRequest(arrayCameraIDPK, arrayPriority)
                        AppLogger.e(data.toString())
                        saveCameraSetting(data)
                    }
                } else {
                    btnSave.isEnabled = false
                }
            }

            R.id.btnDeleteall -> {
                IOSDialog.Builder(this@MyCameraSettingAct)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.delete_msg))
                    .setPositiveButton(
                        getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        AppLogger.e(arrayAdminCameraIDPK.toString())
                        if (arrayAdminCameraIDPK.isNullOrEmpty()) {
                            showErrorToast("There are No camera selected.")
                        } else {
                            AppLogger.e("delete all")
                            deleteCameraSetting()
                        }

                    }
                    .setNegativeButton(
                        getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()
            }
        }
    }

    var groupId: String = ""
    var myGroupList: ArrayList<Groups>? = null
    var myCameraList: ArrayList<CameraDetailsFull>? = null
    var layoutManager: LinearLayoutManager? = null
    var adapter: MyCameraSettingAdapter? = null

    companion object {
        var arrayAdminCameraIDPK = ArrayList<Int>()
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.act_my_camera_sertting
    }

    override fun init() {
        arrayAdminCameraIDPK.clear()
        bindToolBarBack("My Camera Setting")
    }

    override fun initView() {
        layoutManager = LinearLayoutManager(this@MyCameraSettingAct)
        loader.controller = setLoader()
    }

    override fun postInitView() {
    }

    override fun addListener() {
        btnDeleteGroup.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnDeleteall.setOnClickListener(this)
    }

    override fun loadData() {
        val session = SharedPreferenceSession(this@MyCameraSettingAct)
        getGroupList(session.memberType.toString(), session.memberId.toString())
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
                    loader.visibility = View.INVISIBLE
                    myGroupList = response.body()?.groups

                    if (response.body()!!.groups.isEmpty()) {
                        btnSave.isEnabled = false
                        btnDeleteGroup.isEnabled = false
                    }
                    val arrayAdapter =
                        ArrayAdapter<Groups>(
                            this@MyCameraSettingAct,
                            android.R.layout.simple_spinner_dropdown_item,
                            myGroupList
                        )
                    spinnerGroupCamera.adapter = arrayAdapter
                    spinnerGroupCamera?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                if (myGroupList?.get(position)?.cameraDetailsFull.isNullOrEmpty()) {
                                    tvNoData.visibility = View.VISIBLE
                                    recycleviewCameraSettingList.visibility = View.INVISIBLE
                                    btnDeleteall.visibility = View.GONE
                                    groupId = myGroupList?.get(position)!!.groupID.toString()
                                } else {
                                    btnDeleteall.visibility = View.VISIBLE
                                    tvNoData.visibility = View.INVISIBLE
                                    recycleviewCameraSettingList.visibility = View.VISIBLE
                                    recycleviewCameraSettingList.layoutManager = layoutManager
                                    groupId = myGroupList?.get(position)!!.groupID.toString()
                                    adapter = MyCameraSettingAdapter(
                                        this@MyCameraSettingAct,
                                        myGroupList?.get(position)?.cameraDetailsFull
                                    )
                                    recycleviewCameraSettingList.adapter = adapter
                                }
                            }
                        }

                    myCameraList = myGroupList!![0].cameraDetailsFull
                    if (myGroupList!![0].cameraDetailsFull.isEmpty()) {
                        btnDeleteall.visibility = View.GONE
                    } else {
                        btnDeleteall.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<GroupCameraItem>, t: Throwable) {
                hideProgressDialog()
            }

        })

    }

    private fun deleteGroup(groupId: String) {
        val map = HashMap<String, String>()
        map["GroupID"] = groupId
        AppLogger.e("group id $groupId")

        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.deleteGroup(map)

        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                val data = parseJsonObject(response.body().toString())
                if (data.getBoolean("ResponseResult")) {
                    loadData()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })
    }

    private fun deleteCameraSetting() {/*
        val map = HashMap<String, LongArray>()
        val array: LongArray = longArrayOf(31)
        map["AdminCameraID_PK"] = array

//        map["AdminCameraID_PK"] = arrayAdminCameraIDPK
        var str= "AdminCameraID_PKs:"+arrayAdminCameraIDPK
        var gson = Gson()
        AppLogger.e(gson.toJson(data))

*/
        val data = MyCameraSettingDeleteRequest(arrayAdminCameraIDPK)
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.deleteMultipleCamera(data)
        callApi!!.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.e(response.body().toString())
                val data = parseJsonObject(response.body().toString())
                if (data.getBoolean("ResponseResult")) {
                    arrayAdminCameraIDPK.clear()
                    loadData()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }
        })
    }

    private fun saveCameraSetting(data: MyCameraSettingPriorityRequest) {
        showProgressDialog("", "Please wait..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.saveCameraPriority(data)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                hideProgressDialog()
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    val data = parseJsonObject(response.body().toString())
                    if (data.getBoolean("ResponseResult")) {
                        showSuccessToast("Camera priority saved.")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }
}
