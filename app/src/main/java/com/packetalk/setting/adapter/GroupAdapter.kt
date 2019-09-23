package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.Groups
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AssignGroupToUserAct
import com.packetalk.util.AppConstants.GROUP_ID
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_add_group_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Groups>?
) :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_add_group_item, viewGroup, false)
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
        @SuppressLint("SetTextI18n", "RestrictedApi")

        fun setData(data: Groups?) {
            if (adapterPosition == 0) {
                itemView.btnMore.visibility = View.INVISIBLE
            } else {
                itemView.btnMore.visibility = View.VISIBLE
            }
            itemView.tvGroupName.text = data!!.groupName
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.btnMore.setOnClickListener {
                val popupMenu: PopupMenu? =
                    activity?.let { it1 -> PopupMenu(it1, itemView.btnMore) }
                popupMenu?.menuInflater?.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu?.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            showDialog(itemArrayList?.get(adapterPosition)?.groupName.toString().trim())
                        }
                        R.id.action_delete -> {

                            IOSDialog.Builder(activity)
                                .setTitle(activity?.getString(R.string.delete))
                                .setMessage(activity?.getString(R.string.delete_msg))
                                .setPositiveButton(
                                    activity?.getString(R.string.ok)
                                ) { dialog, _ ->
                                    dialog.dismiss()
                                    // continue with delete
                                    AppLogger.e(itemArrayList?.get(adapterPosition)?.groupID.toString())
                                    deleteGroup(itemArrayList?.get(adapterPosition)?.groupID.toString())
                                    removeAt(position = adapterPosition)
                                }
                                .setNegativeButton(
                                    activity?.getString(R.string.cancel)
                                ) { dialog, _ -> dialog.dismiss() }.show()


                        }
                        R.id.action_assign_camera -> {
                            val intent = Intent(activity, AssignGroupToUserAct::class.java)
                            AppLogger.e( itemArrayList?.get(adapterPosition)?.groupID.toString())
                            intent.putExtra(GROUP_ID, itemArrayList?.get(adapterPosition)?.groupID.toString())
                            activity?.startActivity(intent)
                        }
                    }
                    true
                }
                popupMenu?.show()
            }
        }

        override fun onClick(v: View) {
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun editGroup(groupId: String, groupName: String) {
            val map = HashMap<String, String>()
            map["GroupID"] = groupId
            map["Groupname"] = groupName

            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.updateGroup(map)
            callApi!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.e(response.body().toString())

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }

        private fun deleteGroup(groupId: String) {
            val map = HashMap<String, String>()
            map.put("GroupID", groupId)

            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.deleteGroup(map)
            callApi!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.e(response.body().toString())

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }


        private fun showDialog(title: String) {
            val dialog = Dialog(activity)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_group_name_edit)
            val name = dialog.findViewById(R.id.edGroupNames) as TextInputEditText
            name.setText(title)
            val update = dialog.findViewById(R.id.btnUpdate) as Button
            update.setOnClickListener {
                itemArrayList?.get(adapterPosition)!!.groupName = name.text.toString()
                notifyItemChanged(adapterPosition)
                editGroup(
                    itemArrayList?.get(adapterPosition)?.groupID.toString(),
                    itemArrayList?.get(adapterPosition)?.groupName.toString()
                )

                dialog.dismiss()
            }

            dialog.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog.show()


        }


    }


}
