package com.packetalk.home.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.adapter.CameraListAdapter
import com.packetalk.home.adapter.CameraListBottomAdapter
import com.packetalk.home.adapter.GroupListAdapter
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.util.SharedPreferenceSession
import com.packetalk.util.showInfoToast
import kotlinx.android.synthetic.main.frg_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFrg : BaseFragment(), View.OnClickListener {

    var tvSelectGroup: TextView? = null
    private var tvSelectCamera: TextView? = null
    var myGroupList: ArrayList<Groups>? = null
    var myCameraList: ArrayList<CameraDetailsFull>? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var mBottomSheetDialogCamera: BottomSheetDialog? = null
    private lateinit var rootView: View
    var cameraAdapter : CameraListAdapter? = null
    private var linearLayoutManager2 : LinearLayoutManager? = null
    @SuppressLint("InflateParams")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvSelectGroup -> {
                mBottomSheetDialog = activity?.let { BottomSheetDialog(it) }
                val sheetView = layoutInflater.inflate(R.layout.dialog_bottom_selection_group, null)
                mBottomSheetDialog?.setContentView(sheetView)
                val recyclerViewDialogGroup =
                    mBottomSheetDialog?.findViewById<RecyclerView>(R.id.recyclerViewDialogGroup)
                val linearLayoutManager = LinearLayoutManager(activity)
                recyclerViewDialogGroup?.layoutManager = linearLayoutManager
                AppLogger.e("myGroupList$myGroupList")
                val adapter = GroupListAdapter(activity, HomeFrg(), myGroupList)
                recyclerViewDialogGroup?.adapter = adapter
                adapter.onItemClick = { groups ->
                    AppLogger.e(groups.groupName)
                    dismissDialog(groups.groupName)
                    myCameraList = groups.cameraDetailsFull
                    if (groups.cameraDetailsFull.isEmpty()) {
                        tvNodata.visibility = View.VISIBLE
                        recycleViewCameras.visibility = View.INVISIBLE
                    } else {
                        tvNodata.visibility = View.INVISIBLE
                        recycleViewCameras.visibility = View.VISIBLE
                        cameraAdapter =
                            CameraListAdapter(activity, HomeFrg(), groups.cameraDetailsFull)
                        recycleViewCameras?.adapter = cameraAdapter
                    }
                }
                mBottomSheetDialog?.show()
            }

            R.id.tvSelectCamera -> {
                mBottomSheetDialogCamera = activity?.let { BottomSheetDialog(it) }
                val sheetView =
                    layoutInflater.inflate(R.layout.dialog_bottom_selection_camera, null)
                mBottomSheetDialogCamera?.setContentView(sheetView)
                val recyclerViewDialogCamera =
                    mBottomSheetDialogCamera?.findViewById<RecyclerView>(R.id.recyclerViewDialogCamera)
                val linearLayoutManager = LinearLayoutManager(activity)
                recyclerViewDialogCamera?.layoutManager = linearLayoutManager
                if (myCameraList?.isEmpty()!!) {
                    activity?.showInfoToast("No camera available")
                } else {
                    val adapter = CameraListBottomAdapter(activity, HomeFrg(), myCameraList)
                    recyclerViewDialogCamera?.adapter = adapter

                    adapter.onItemClick = { cameraDetailsFull ->
                        mBottomSheetDialogCamera?.dismiss()
                        val cameraItem: CameraDetailsFull? = cameraDetailsFull
                        val intent = Intent(activity, CameraDetailAct::class.java)
                        intent.putExtra(AppConstants.CAMERA_DETAIL_KEY, cameraItem)
                        activity?.startActivity(intent)
                    }
                    mBottomSheetDialogCamera?.show()
                }
            }
        }
    }

    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_home, parent, false)
        return rootView
    }

    override fun init() {
        tvSelectGroup = rootView.findViewById(R.id.tvSelectGroup)
        tvSelectCamera = rootView.findViewById(R.id.tvSelectCamera)
        linearLayoutManager2 = LinearLayoutManager(activity)
        recycleViewCameras?.layoutManager = linearLayoutManager2
    }

    override fun initView() {

    }

    override fun postInitView() {
    }

    override fun addListener() {
        tvSelectGroup?.setOnClickListener(this)
        tvSelectCamera?.setOnClickListener(this)
    }

    override fun loadData() {
        val session = activity?.let { SharedPreferenceSession(it) }
        getGroupList(session?.memberType.toString(), session?.memberId.toString())
    }

    private fun dismissDialog(groupName: String) {
        AppLogger.e(groupName)
        tvSelectGroup!!.text = groupName.trim()
        mBottomSheetDialog?.dismiss()
    }

    private fun getGroupList(memberType: String, memberId: String) {
        showProgressDialog("Camera", "Please wait..")
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
                    myGroupList = response.body()?.groups
                    tvSelectGroup!!.text = myGroupList?.get(0)!!.groupName.trim()
                    if (myGroupList!![0].cameraDetailsFull.isEmpty()) {
                        myCameraList = myGroupList!![0].cameraDetailsFull
                        tvNodata.visibility = View.VISIBLE
                        recycleViewCameras.visibility = View.INVISIBLE
                    } else {
                        tvNodata.visibility = View.INVISIBLE
                        recycleViewCameras.visibility = View.VISIBLE
                        myCameraList = myGroupList!![0].cameraDetailsFull
                        cameraAdapter =
                            CameraListAdapter(activity, HomeFrg(), myCameraList)
                        recycleViewCameras?.adapter = cameraAdapter
                    }

                }


//                AppLogger.e(myGroupList.toString())
/*
                val d = JSONObject(response.body().toString())

                val json = d.get("d")
                AppLogger.e("json-------------$json")

                val obj = JSONArray(json.toString())
                AppLogger.e("obj-------------$obj")

//                AppLogger.e("length-----------${obj.length()}")

                val listType: Type = object : TypeToken<ArrayList<GroupListItem>>() {
                }.type

                myGroupList = Gson().fromJson(json.toString(), listType)*/


            }

            override fun onFailure(call: Call<GroupCameraItem>, t: Throwable) {
                hideProgressDialog()
            }
        })

    }

}

