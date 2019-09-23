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
import com.packetalk.setting.model.server_setting.Object
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_server_setting_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerSettingAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<ServerSettingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_server_setting_item, viewGroup, false)
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

        fun setData(data: Object?) {
            itemView.tvServerName.text = "${data!!.uri}:${data.port}"
            itemView.switchServer.isChecked = data.isDeleted == 0

        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.switchServer.setOnCheckedChangeListener { _, isChecked ->
                itemArrayList?.get(adapterPosition)!!.routerStatus = isChecked
                disableServerRouter(
                    itemArrayList!![adapterPosition].uri,
                    itemArrayList!![adapterPosition].isDeleted.toString()
                )
            }
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
//            val intent = Intent(activity,CameraDetailAct::class.java)
//            activity?.startActivity(intent)
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun disableServerRouter(serverUrl: String, isDeleted: String) {
            val map = HashMap<String, String>()
            map["ServerURL"] = serverUrl
            map["isDeleted"] = isDeleted

            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.disableServerRouter(map)
            callApi!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.e(response.body().toString())

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })
        }

    }

}
