package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.model.add_camera.ObjectX
import com.packetalk.setting.model.server_setting.Object
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_server_setting_item.view.*
import kotlinx.android.synthetic.main.dialog_add_camera_item_exsting_url.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExistingUrlAdapter(private var itemArrayList: ArrayList<ObjectX>?) :

    RecyclerView.Adapter<ExistingUrlAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_add_camera_item_exsting_url, viewGroup, false)
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

        fun setData(data: ObjectX?) {
            itemView.tvExistingUrl.text = data!!.uri
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
//            removeServerUrl(itemArrayList?.get(adapterPosition)!!.uri,itemArrayList!![adapterPosition].isDeleted as String)
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun removeServerUrl(serverUrl: String, isDeleted: String) {

            val map = HashMap<String, String>()
            map["ServerURL"] = serverUrl
            map["IsDeleted"] = isDeleted
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.removeCameraServer(map)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                    }
                }

            })
        }

    }

}
