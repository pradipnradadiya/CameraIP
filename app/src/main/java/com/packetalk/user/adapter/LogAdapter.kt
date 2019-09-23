package com.packetalk.user.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.user.model.log.Object
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_home_item_camera.view.*
import kotlinx.android.synthetic.main.frg_user_log_item.view.*

class LogAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<LogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frg_user_log_item, viewGroup, false)
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
            itemView.tvLogDetail.text = data!!.desc
            itemView.tvLog.text = data.action
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
//            val intent = Intent(activity,CameraDetailAct::class.java)
//            activity?.startActivity(intent)
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
