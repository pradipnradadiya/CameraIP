package com.packetalk.user.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.user.activity.AssignCameraAct
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_assign_camera_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssignCameraAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<AssignCameraAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_assign_camera_item, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
//        AppLogger.e("position$i")
        val data = itemArrayList?.get(i)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return itemArrayList!!.size
    }

    fun removeAt(position: Int) {
        itemArrayList?.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        @SuppressLint("SetTextI18n")

        fun setData(data: CameraDetailsFull?) {
            itemView.tvCamName.text = data!!.cameraName
            AppLogger.e("choice ${data.choice}")

            if (data.choice) {
                itemView.tvCamName.setTextColor(activity?.resources!!.getColor(R.color.colorPrimary))
            } else {
                itemView.tvCamName.setTextColor(activity?.resources!!.getColor(R.color.black))
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")

            if (itemArrayList?.get(adapterPosition)?.choice!!) {
                removeAt(adapterPosition)
                (activity as AssignCameraAct).removeAt(adapterPosition)
            } else {
                removeAt(adapterPosition)
                (activity as AssignCameraAct).removeAt(adapterPosition)
            }


        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

    }

    private fun deleteAssignCamera(id: String, adminCameraId: String) {
        val map = HashMap<String, String>()
        map["MemberID"] = id
        map["MemberID"] = adminCameraId

        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.deleteAssignCamera(map)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            }

        })
    }


}
