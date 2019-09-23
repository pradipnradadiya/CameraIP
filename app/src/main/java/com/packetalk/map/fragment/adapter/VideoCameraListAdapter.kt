package com.packetalk.map.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.map.fragment.MapFrg
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.dialog_map_camera_list_item.view.*


class VideoCameraListAdapter(
    private val activity: FragmentActivity?,
    public var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<VideoCameraListAdapter.ViewHolder>() {
    companion object{
        var camPosition:Int = 0
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_map_camera_list_item, viewGroup, false)
        return ViewHolder(v)
    }
    var onItemClick: ((CameraDetailsFull) -> Unit)? = null
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
            itemView.tvCam.text = "Cam${adapterPosition + 1}"
            val pulse = AnimationUtils.loadAnimation(activity, R.anim.pulse)
            itemView.imgVideo.startAnimation(pulse)
            if (data?.isSelected!!){
                itemView.tvCam.setTextColor(activity?.resources!!.getColor(R.color.colorPrimary))
            }else{
                itemView.tvCam.setTextColor(activity?.resources!!.getColor(R.color.black))
            }

        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
            for ((index, value) in itemArrayList?.withIndex()!!) {
                itemArrayList!![index].isSelected = index == adapterPosition
            }

            camPosition = adapterPosition
            onItemClick?.invoke(itemArrayList!![adapterPosition])

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }


    }

}
