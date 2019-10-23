package com.packetalk.map.fragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.fragment.HomeFrg
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import kotlinx.android.synthetic.main.frg_map_item_camera_view.view.*

class CameraViewListAdapter(
    private val activity: FragmentActivity?,
    var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<CameraViewListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frg_map_item_camera_view, viewGroup, false)
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
            itemView.tvCamName.text = data!!.cameraName
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            removeAt(adapterPosition)

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
