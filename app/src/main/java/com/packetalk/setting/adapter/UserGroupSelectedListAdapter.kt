package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.R
import com.packetalk.home.activity.HomeAct.Companion.userId
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AssignGroupToUserAct
import com.packetalk.user.model.users.Object
import com.packetalk.util.AppLogger
import com.packetalk.util.parseJsonObject
import com.packetalk.util.showSuccessToast
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import kotlinx.android.synthetic.main.act_assign_group_to_user_item.view.*
import kotlinx.android.synthetic.main.frg_create_user_item.view.tvUserName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class UserGroupSelectedListAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<UserGroupSelectedListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_assign_group_to_user_item, viewGroup, false)
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
            if (data.isNew) {
                itemView.linCamera.setBackgroundResource(R.drawable.button_border)
                itemView.tvUserName.setTextColor(activity?.resources?.getColor(R.color.white)!!)
                itemView.imgClose.setColorFilter(
                    ContextCompat.getColor(activity, R.color.white),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            } else {
                itemView.linCamera.setBackgroundResource(R.drawable.card_border)
                itemView.tvUserName.setTextColor(activity?.resources?.getColor(R.color.black)!!)
                itemView.imgClose.setColorFilter(
                    ContextCompat.getColor(activity, R.color.black),
                    android.graphics.PorterDuff.Mode.MULTIPLY
                )
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")

            if (itemArrayList?.get(adapterPosition)?.isNew!!) {

                val mBottomSheetDialog =
                    BottomSheetMaterialDialog.Builder(activity as AppCompatActivity)
                        .setTitle(activity.getString(R.string.delete))
                        .setMessage(activity.getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(activity.getString(R.string.ok),R.drawable.ic_delete_popup) { dialogInterface, which ->
                            dialogInterface.dismiss()
                            removeAt(adapterPosition)
                        }
                        .setNegativeButton(activity.getString(R.string.cancel),R.drawable.ic_close) { dialogInterface, which ->
                            dialogInterface.dismiss()
                        }
                        .build()
                // Show Dialog
                mBottomSheetDialog.show()

/*
                IOSDialog.Builder(activity)
                    .setTitle(activity?.getString(R.string.delete))
                    .setMessage(activity?.getString(R.string.delete_msg))
                    .setPositiveButton(
                        activity?.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                       removeAt(adapterPosition)

                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()

*/

            }else{
                val mBottomSheetDialog =
                    BottomSheetMaterialDialog.Builder(activity as AppCompatActivity)
                        .setTitle(activity.getString(R.string.delete))
                        .setMessage(activity.getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(activity.getString(R.string.ok),R.drawable.ic_delete_popup) { dialogInterface, which ->
                            dialogInterface.dismiss()
                            // continue with delete
                            AppLogger.e(itemArrayList!![adapterPosition].iD)
                            deleteUserOnGroup((activity as AssignGroupToUserAct).groupId.toString(),itemArrayList!![adapterPosition].iD)
                            removeAt(adapterPosition)
                        }
                        .setNegativeButton(activity.getString(R.string.cancel),R.drawable.ic_close) { dialogInterface, which ->
                            dialogInterface.dismiss()
                        }
                        .build()
                // Show Dialog
                mBottomSheetDialog.show()

                /*
                IOSDialog.Builder(activity)
                    .setTitle(activity?.getString(R.string.delete))
                    .setMessage(activity?.getString(R.string.delete_msg))
                    .setPositiveButton(
                        activity?.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        AppLogger.e(itemArrayList!![adapterPosition].iD)
                        deleteUserOnGroup(itemArrayList!![adapterPosition].iD, userId)
                        removeAt(adapterPosition)

                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()
                */

            }

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }


    }

    private fun deleteUserOnGroup(groupId: String, memberId: String) {
        val map = HashMap<String, String>()
        map["GroupID"] = groupId
        map["MemberID"] = memberId
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.deleteGroupAssignedUsers(map)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.response(response.body().toString())
                val json = parseJsonObject(response.body().toString())
                if (json.getBoolean("ResponseResult")) {

                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                AppLogger.response(t.message.toString())

            }

        })
    }

}
