package com.packetalk.user.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.map.fragment.MapFrg
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.dialog_assign_camera_act_item.view.*
import kotlinx.android.synthetic.main.dialog_map_camera_list_item.view.*


class AssignGroupListAdapter(
    private val activity: FragmentActivity?,
    public var itemArrayList: ArrayList<Groups>?
) :
    RecyclerView.Adapter<AssignGroupListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_assign_camera_act_item, viewGroup, false)
        return ViewHolder(v)
    }
    var onItemClick: ((Groups,Int) -> Unit)? = null

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

        fun setData(data: Groups?) {
            itemView.tvGroupName.text = data?.groupName ?: ""
            if (data?.isSelected!!){
                itemView.tvGroupName.setBackgroundColor(Color.GRAY)
            }else{
                itemView.tvGroupName.setBackgroundColor(Color.WHITE)
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

        }

        override fun onClick(v: View) {
            for ((index, value) in itemArrayList?.withIndex()!!) {
//                itemArrayList!![index].isSelected = index == adapterPosition
                itemArrayList!![index].isSelected = adapterPosition == index
                notifyItemChanged(index)

            }
         //   notifyDataSetChanged()
            onItemClick?.invoke(itemArrayList!![adapterPosition],adapterPosition)

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

    }

}

