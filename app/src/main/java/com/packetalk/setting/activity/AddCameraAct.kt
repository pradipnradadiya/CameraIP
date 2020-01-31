package com.packetalk.setting.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.view.View
import android.view.Window
import android.webkit.URLUtil
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.DefaultGroupAdapter
import com.packetalk.setting.adapter.ExistingUrlAdapter
import com.packetalk.setting.adapter.GroupCameraAdapter
import com.packetalk.setting.model.add_camera.CamToGroupRequest
import com.packetalk.setting.model.add_camera.CameraUrlItem
import com.packetalk.setting.model.add_camera.DefaultCameraItem
import com.packetalk.setting.model.add_camera.ObjectX
import com.packetalk.util.*
import com.packetalk.utility.SwipeDismissTouchListener
import kotlinx.android.synthetic.main.act_add_camera.*
import kotlinx.android.synthetic.main.dialog_add_camera.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.MalformedURLException


class AddCameraAct : BaseActivity(), View.OnClickListener {

    var existingUrlList: ArrayList<ObjectX>? = null
    var myCameraList: ArrayList<CameraDetailsFull>? = null
    var saveCamList = ArrayList<CameraDetailsFull>()
    var defaultCameraList: ArrayList<CameraDetailsFull>? = null
    var layoutManager: LinearLayoutManager? = null
    var layoutManager1: LinearLayoutManager? = null
    var adapter: GroupCameraAdapter? = null
    var existingUrlAdapter: ExistingUrlAdapter? = null
    var flag: Boolean = false
    lateinit var dialog: Dialog
    private var groupId: String = ""

    companion object {
        var groupPosition: Int = 0
        var myGroupList: ArrayList<Groups>? = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                saveCam()
            }
            R.id.btnCancel -> {
                showDialog()
            }
            R.id.tvSelectAll -> {

                if (myGroupList?.get(groupPosition)!!.cameraDetailsFull.isNullOrEmpty()) {
                    for ((pos, data) in defaultCameraList?.withIndex()!!) {
                        data.choice = true
                        myGroupList?.get(groupPosition)?.cameraDetailsFull?.add(data)
                    }
                } else {
                    AppLogger.e(defaultCameraList!!.size.toString())
                    AppLogger.e(myGroupList?.get(groupPosition)!!.cameraDetailsFull.size.toString())

                    for ((j, defaultList) in defaultCameraList?.withIndex()!!) {
                        defaultList.choice = true
                        myGroupList?.get(groupPosition)?.cameraDetailsFull?.add(defaultList)
                    }

                    val set = HashSet(myGroupList!![groupPosition].cameraDetailsFull)
                    AppLogger.e("size------${myGroupList!![groupPosition].cameraDetailsFull.size}")
                    myGroupList!![groupPosition].cameraDetailsFull.clear()
                    myGroupList!![groupPosition].cameraDetailsFull.addAll(set)

                    AppLogger.e("size------${myGroupList!![groupPosition].cameraDetailsFull.size}")

                    /*
                    for (i in 0 until defaultCameraList!!.size) {

                        for (j in 0 until myGroupList?.get(groupPosition)!!.cameraDetailsFull.size) {

                            AppLogger.e(myGroupList?.get(groupPosition)?.cameraDetailsFull?.get(j)?.cameraName.toString())
                            AppLogger.e(defaultCameraList!![j].cameraName.toString())

                            if (!myGroupList?.get(groupPosition)?.cameraDetailsFull?.get(j)?.cameraName?.trim().equals(
                                    defaultCameraList!![j].cameraName.trim()
                                )
                            ) {
                                //do something for not equals
                                AppLogger.e("false")
                            } else {
                                //do something for equals
                                AppLogger.e("true")
                                AppLogger.e(myGroupList?.get(groupPosition)?.cameraDetailsFull?.get(j)?.cameraName.toString())
                            }
                        }
                    }*/

/*
                    for ((i, cameraList) in myGroupList?.get(groupPosition)!!.cameraDetailsFull.withIndex()) {
                        AppLogger.e("i loop-----$i")

                        for ((j, defaultList) in defaultCameraList?.withIndex()!!) {
                            AppLogger.e("j loop-----$j")

                            if (defaultCameraList!![j].cameraName.trim() != myGroupList?.get(groupPosition)!!.cameraDetailsFull[i].cameraName.trim()) {
                                myGroupList?.get(groupPosition)?.cameraDetailsFull?.add(defaultList)
                            }

                        }
                    }*/
                }

                refreshAdapter()

            }
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.act_add_camera
    }

    override fun init() {
        bindToolBarBack("Add Camera")
    }

    override fun initView() {
        layoutManager = LinearLayoutManager(this@AddCameraAct)
        layoutManager1 = LinearLayoutManager(this@AddCameraAct)
        loader.controller = setLoader()
        dialog = Dialog(this@AddCameraAct)
    }

    override fun postInitView() {
    }

    override fun addListener() {
        tvSelectAll.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun loadData() {
        val session = SharedPreferenceSession(this@AddCameraAct)
        getGroupList(session.memberType.toString(), session.memberId.toString())
        getDefaultGroupList()
        getCameraUrl()
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
                    loader.invisible()
                    myGroupList = response.body()?.groups
                    groupId = response.body()!!.groups[0].groupID.toString()


                    val arrayAdapter =
                        myGroupList?.let {
                            ArrayAdapter<Groups>(
                                this@AddCameraAct,
                                android.R.layout.simple_spinner_dropdown_item,
                                it
                            )
                        }


                    spinnerGroup.adapter = arrayAdapter
                    spinnerGroup?.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }

                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                groupPosition = position
                                groupId = response.body()!!.groups[position].groupID.toString()
                                if (myGroupList?.get(position)?.cameraDetailsFull.isNullOrEmpty()) {
                                    recycleViewMyCamera.invisible()
                                } else {
                                    recycleViewMyCamera.visible()
                                    recycleViewMyCamera.layoutManager = layoutManager
                                    adapter = GroupCameraAdapter(
                                        this@AddCameraAct,
                                        myGroupList?.get(position)?.cameraDetailsFull
                                    )
                                    recycleViewMyCamera.adapter = adapter
                                }
                            }
                        }
                    myCameraList = myGroupList!![0].cameraDetailsFull
                }
            }

            override fun onFailure(call: Call<GroupCameraItem>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }

    fun getDefaultGroupList() {
        showProgressDialog("Default Group", "Please wait..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getDefaultGroupList()
        callApi!!.enqueue(object : Callback<DefaultCameraItem> {

            override fun onResponse(
                call: Call<DefaultCameraItem>,
                response: Response<DefaultCameraItem>
            ) {
                AppLogger.e(response.body().toString())
                hideProgressDialog()
                if (response.body()!!.responseResult) {
                    recycleViewDefaultGroup.layoutManager = layoutManager1
                    defaultCameraList = response.body()!!.objectX
                    val adapter = DefaultGroupAdapter(this@AddCameraAct, defaultCameraList)
                    recycleViewDefaultGroup.adapter = adapter
                }
            }

            override fun onFailure(call: Call<DefaultCameraItem>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }

    fun refreshAdapter() {
        recycleViewMyCamera.visible()
        recycleViewMyCamera.layoutManager = layoutManager
        adapter = GroupCameraAdapter(
            this@AddCameraAct,
            myGroupList?.get(groupPosition)?.cameraDetailsFull
        )
        recycleViewMyCamera.adapter = adapter

        spinnerGroup.isEnabled = false

        (spinnerGroup.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.expand_title_color))
        spinnerGroup.isEnabled = false


    }

    fun checkSpinnerEnableDisable(position: Int) {
        //  spinnerGroup.isEnabled = position != 1

        var flag = false
        for ((index, value) in adapter?.itemArrayList?.withIndex()!!) {
            if (value.choice) {
                flag = true
                break
            } else {
                flag = false
            }
        }

//        spinnerGroup.isEnabled = !flag
        if (flag) {
            (spinnerGroup.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.expand_title_color))
            spinnerGroup.isEnabled = false

        } else {
            (spinnerGroup.getChildAt(0) as TextView).setTextColor(resources.getColor(R.color.black))
            spinnerGroup.isEnabled = true
        }


        /*
  //        myGroupList?.get(groupPosition)?.cameraDetailsFull?.removeAt(position)
          for ((index, value) in myGroupList?.get(groupPosition)?.cameraDetailsFull?.withIndex()!!) {
              spinnerGroup.isEnabled =
                  !myGroupList?.get(groupPosition)?.cameraDetailsFull!![index].choice
          }*/
    }

    private fun saveCam() {

        saveCamList.clear()
        for (data in myGroupList?.get(groupPosition)?.cameraDetailsFull!!) {
            if (data.choice) {
                AppLogger.e(data.toString())
                saveCamList?.add(data)
            }
        }
        if (saveCamList?.isEmpty()!!) {
            showInfoToast("There are no new camera for submit!")
        } else {
            showProgressDialog("Assign Camera", "Please wait..")
            val req = CamToGroupRequest(groupId, saveCamList!!)
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.assignCameraToGroup(req)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    hideProgressDialog()
                    showWarningToast("Something want to wrong.")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.response(response.body().toString())
                    if (response.isSuccessful) {
                        val json = parseJsonObject(response.body().toString())
                        if (json.getBoolean("ResponseResult")) {
                            showSuccessToast("Camera assigned successfully.")
                            hideProgressDialog()
                        }
                    }
                }

            })
        }

    }

    private fun isValid(urlString: String): Boolean {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (e: MalformedURLException) {
        }
        return false
    }

    private fun showDialog() {

        dialog.setContentView(R.layout.dialog_add_camera)

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
//        val name = dialog.findViewById(R.id.edGroupNames) as TextInputEditText
//        name.setText(title)
        dialog.btnUpdateCamera.setOnClickListener {
            if (isValid(dialog.edUrl.text.toString())) {
                addCamera("https://bcpo.packetalk.net/grapesxml/bcpo1.packetalk.net:5350.php")
            } else {
                showErrorToast("Please enter valid url.")
            }


            //  addCamera("https://willingboro.packetalk.net/grapesxml/willingboro1.packetalk.net:5350.php")
        }
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val layoutManager = LinearLayoutManager(this@AddCameraAct)
        dialog.recycleViewExstingUrl.layoutManager = layoutManager
        dialog.recycleViewExstingUrl.adapter = existingUrlAdapter
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


        val window: Window? = dialog.window
        window?.decorView?.setOnTouchListener(
            SwipeDismissTouchListener(
                window.decorView,
                null,
                object : SwipeDismissTouchListener.DismissCallbacks {
                    override fun onDismiss(view: View?, token: Any?) {
                        dialog.dismiss()
                    }

                    override fun canDismiss(token: Any?): Boolean {
                        return true
                    }

                })
        )

    }

    private fun addCamera(url: String) {
        showProgressDialog("", getString(R.string.please_wait))
        val map = HashMap<String, String>()
        map["url"] = url
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.addUpdateCamera(map)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.response(response.body().toString())
                hideProgressDialog()
                if (response.isSuccessful) {
                    val json = parseJsonObject(response.body().toString())
                    if (json.getBoolean("ResponseResult")) {
                        showSuccessToast("Camera update successfully.")
                        dialog.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.error(t.toString())
                hideProgressDialog()
            }

        })
    }

    private fun getCameraUrl() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getCameraURL()
        callApi?.enqueue(object : Callback<CameraUrlItem> {
            override fun onFailure(call: Call<CameraUrlItem>, t: Throwable) {

            }

            override fun onResponse(call: Call<CameraUrlItem>, response: Response<CameraUrlItem>) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        existingUrlList = response.body()!!.objectX
                        existingUrlAdapter = ExistingUrlAdapter(existingUrlList)
                    }
                }
            }

        })
    }
}