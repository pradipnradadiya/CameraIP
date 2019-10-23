package com.packetalk.setting.activity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.adapter.DefaultGroupAdapter
import com.packetalk.setting.adapter.GroupCameraAdapter
import com.packetalk.setting.model.add_camera.DefaultCameraItem
import com.packetalk.util.AppLogger
import com.packetalk.util.SharedPreferenceSession
import com.packetalk.util.setLoader
import kotlinx.android.synthetic.main.act_add_camera.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddCameraAct : BaseActivity(), View.OnClickListener {

    var myCameraList: ArrayList<CameraDetailsFull>? = null
    var defaultCameraList: ArrayList<CameraDetailsFull>? = null
    var layoutManager: LinearLayoutManager? = null
    var layoutManager1: LinearLayoutManager? = null
    var adapter: GroupCameraAdapter? = null
    var flag: Boolean = false

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
                                if (myGroupList?.get(position)?.cameraDetailsFull.isNullOrEmpty()) {
                                    recycleViewMyCamera.visibility = View.INVISIBLE
                                } else {
                                    recycleViewMyCamera.visibility = View.VISIBLE
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
        recycleViewMyCamera.visibility = View.VISIBLE
        recycleViewMyCamera.layoutManager = layoutManager
        adapter = GroupCameraAdapter(
            this@AddCameraAct,
            myGroupList?.get(groupPosition)?.cameraDetailsFull
        )
        recycleViewMyCamera.adapter = adapter
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
        spinnerGroup.isEnabled = !flag

        /*
  //        myGroupList?.get(groupPosition)?.cameraDetailsFull?.removeAt(position)
          for ((index, value) in myGroupList?.get(groupPosition)?.cameraDetailsFull?.withIndex()!!) {
              spinnerGroup.isEnabled =
                  !myGroupList?.get(groupPosition)?.cameraDetailsFull!![index].choice
          }*/
    }

    private fun saveCam() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
//        val callApi = apiInterface?.saveCameraPriority()
    }
}
