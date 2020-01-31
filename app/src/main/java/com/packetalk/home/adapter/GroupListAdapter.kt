package com.packetalk.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.fragment.HomeFrg
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.util.AppLogger
import com.packetalk.util.showInfoToast
import kotlinx.android.synthetic.main.dialog_bottom_selection_item_select_group.view.*

class GroupListAdapter(
    private val activity: FragmentActivity?, var homeFrg: HomeFrg,
    private var itemArrayList: ArrayList<Groups>?
) :
    RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_bottom_selection_item_select_group, viewGroup, false)
        return ViewHolder(v)
    }

    var onItemClick: ((Groups) -> Unit)? = null

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
            itemView.tvGroupName.text = data!!.groupName.trim()
            if (data.isSelected) {
                itemView.tvGroupName.setTextColor(itemView.resources.getColor(R.color.red))
            } else {
                itemView.tvGroupName.setTextColor(itemView.resources.getColor(R.color.black))
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
//            itemView.btnDelete.setOnClickListener {
//                deleteReferral(itemArrayList[adapterPosition].entityId)
//            }
        }

        override fun onClick(v: View) {
            for ((index, value) in itemArrayList?.withIndex()!!) {
                itemArrayList!![index].isSelected = index == adapterPosition
            }
//            notifyDataSetChanged()
            print(adapterPosition)
//            print("group names-----------"+data.groupName)

//            HomeFrg().dismissDialog(itemArrayList!![adapterPosition].groupName)
//            (activity as HomeFrg).dismissDialog()

            onItemClick?.invoke(itemArrayList!![adapterPosition])


        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
