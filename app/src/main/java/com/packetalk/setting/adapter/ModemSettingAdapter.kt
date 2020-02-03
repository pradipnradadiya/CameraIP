package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.model.modem_setting.Object
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_modem_setting_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModemSettingAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<ModemSettingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_modem_setting_item, viewGroup, false)
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
            itemView.tvName.text = data!!.uri
            itemView.switchModem.isChecked = data.isDeleted == 1

            if (data.routerStatus) {
                itemView.tvStatus.setBackgroundResource(R.drawable.bg_green_circle)
            } else if (!data.routerStatus) {
                itemView.tvStatus.setBackgroundResource(R.drawable.bg_red_circle)
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

            itemView.switchModem.setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView?.isPressed!!) {
                   // itemArrayList?.get(adapterPosition)!!.routerStatus = isChecked
                    if (itemArrayList?.get(adapterPosition)!!.isDeleted == 0) {
                        itemArrayList?.get(adapterPosition)!!.isDeleted = 1
                    } else {
                        itemArrayList?.get(adapterPosition)!!.isDeleted = 0
                    }
                    disableModemRouter(
                        itemArrayList!![adapterPosition].trailerID.toString(),
                        itemArrayList!![adapterPosition].isDeleted
                    )

                }
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

        private fun disableModemRouter(routerId: String, isDeleted: Int) {
            val map = HashMap<String, Any?>()
            map["RouterID"] = routerId
            map["isDeleted"] = isDeleted

            AppLogger.e("" + map)
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.disableModemRouter(map)
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
