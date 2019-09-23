package com.packetalk.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.fragment.HomeFrg
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.GroupCameraItem
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.util.AppLogger
import com.packetalk.util.showInfoToast
import kotlinx.android.synthetic.main.dialog_bottom_selection_item_select_camera.view.*
import kotlinx.android.synthetic.main.dialog_bottom_selection_item_select_group.view.*

class CameraListBottomAdapter(
    private val activity: FragmentActivity?, var homeFrg: HomeFrg,
    private var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<CameraListBottomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_bottom_selection_item_select_camera, viewGroup, false)
        return ViewHolder(v)
    }

    var onItemClick: ((CameraDetailsFull) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
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
            itemView.tvCameraName.text = data!!.cameraName
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {

            onItemClick?.invoke(itemArrayList!![adapterPosition])

//            var intent = Intent(activity, CameraDetailAct::class.java)
//            activity?.startActivity(intent)
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
