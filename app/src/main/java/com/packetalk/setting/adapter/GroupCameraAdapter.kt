package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AddCameraAct
import com.packetalk.setting.activity.AddCameraAct.Companion.groupPosition
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.act_add_camera.*
import kotlinx.android.synthetic.main.act_assign_camera_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupCameraAdapter(
    private val activity: FragmentActivity?,
    public var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<GroupCameraAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_assign_camera_item, viewGroup, false)
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

        fun setData(data: CameraDetailsFull?) {
            itemView.tvCamName.text = data!!.cameraName
            AppLogger.e("choice ${data.choice}")

            if (data.choice) {
                itemView.tvCamName.setTextColor(activity?.resources!!.getColor(R.color.colorPrimary))
            } else {
                itemView.tvCamName.setTextColor(activity?.resources!!.getColor(R.color.black))
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")

            if (itemArrayList?.get(adapterPosition)?.choice!!) {

                IOSDialog.Builder(activity)
                    .setTitle(activity?.getString(R.string.delete))
                    .setMessage(activity?.getString(R.string.delete_msg))
                    .setPositiveButton(
                        activity?.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        AppLogger.e(adapterPosition.toString())
                        removeAt(adapterPosition)
                        AppLogger.e("group position $groupPosition")
                        AppLogger.e("adapter position $adapterPosition")
//                myGroupList?.get(groupPosition)?.cameraDetailsFull?.removeAt(adapterPosition)

                        (activity as AddCameraAct).checkSpinnerEnableDisable(0)

                        /* for ((index, value) in itemArrayList?.withIndex()!!) {

                             if (itemArrayList!![index].choice){

                                 break
                             }else{

                             }

                         }*/
                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()

            } else {
                IOSDialog.Builder(activity)
                    .setTitle(activity?.getString(R.string.delete))
                    .setMessage(activity?.getString(R.string.delete_msg))
                    .setPositiveButton(
                        activity?.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        deleteCamera(itemArrayList!![adapterPosition].adminCameraIDPK)
                        removeAt(adapterPosition)
//                myGroupList?.get(groupPosition)?.cameraDetailsFull?.removeAt(adapterPosition)
                        (activity as AddCameraAct).checkSpinnerEnableDisable(1)
                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()
            }

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

    private fun deleteCamera(adminCameraId: Int) {
        val dialog = ProgressDialog(activity)
        dialog.setMessage("Camera move to default.")
        dialog.setCancelable(false)
        dialog.show()
        val map = HashMap<String, Int>()
        map["AdminCameraID_PK"] = adminCameraId
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.moveCameraToDefaultGroup(map)
        callApi?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                (activity as AddCameraAct).getDefaultGroupList()
                dialog.dismiss()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                dialog.dismiss()
            }

        })
    }

}
