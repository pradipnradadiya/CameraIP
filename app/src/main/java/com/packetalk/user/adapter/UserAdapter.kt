package com.packetalk.user.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.user.activity.AssignCameraAct
import com.packetalk.user.model.users.Object
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_create_user_item.view.*

class UserAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frg_create_user_item, viewGroup, false)
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
            itemView.tvUserName.text = data!!.username
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.tvAssignCamera.setOnClickListener {
                val intent = Intent(activity,AssignCameraAct::class.java)
                intent.putExtra(AppConstants.MemberID, itemArrayList?.get(adapterPosition)?.iD)
                intent.putExtra(AppConstants.MEMBERNAME, itemArrayList?.get(adapterPosition)?.username)
                activity?.startActivity(intent)
            }
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

    }
}
