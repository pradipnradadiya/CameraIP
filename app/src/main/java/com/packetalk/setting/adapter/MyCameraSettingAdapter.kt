package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.MyCameraSettingAct.Companion.arrayAdminCameraIDPK
import com.packetalk.setting.request.trailor.my_camera_setting.MyCameraSettingDeleteRequest
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.util.parseJsonObject
import com.packetalk.util.showSuccessToast
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import kotlinx.android.synthetic.main.act_my_camera_setting_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyCameraSettingAdapter(
    private val activity: FragmentActivity?,
    public var itemArrayList: ArrayList<CameraDetailsFull>?
) : RecyclerView.Adapter<MyCameraSettingAdapter.ViewHolder>() {

    var arrayList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_my_camera_setting_item, viewGroup, false)
        return ViewHolder(v)
    }

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
            val url =
                "${AppConstants.HTTP_BIND}${data.serverURL}:${data.serverPort}/viewpanel/${data.cameraIDOnServer}/viewpanel&imagewidth=90&imageheight=100"
            activity?.let { itemView.webCamera.load(url, it) }
            val arrayAdapter =
                activity?.let {
                    ArrayAdapter<String>(
                        it,
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayList
                    )
                }
            itemView.spinnerPriority.adapter = arrayAdapter
            AppLogger.e((data.priority - 1).toString())
            itemView.spinnerPriority.setSelection((data.priority) - 1)
            itemView.checkBox.isChecked = data.isSelected
            itemView.spinnerPriority?.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        itemArrayList?.get(adapterPosition)!!.priority = position + 1
                    }
                }
        }

        init {
            arrayList.clear()
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

            if (!itemArrayList.isNullOrEmpty()) {
                for ((index, value) in itemArrayList?.withIndex()!!) {
                    arrayList.add((index + 1).toString())
                }
            }

            itemView.linDelete.setOnClickListener {

                val mBottomSheetDialog =
                    BottomSheetMaterialDialog.Builder(activity as AppCompatActivity)
                        .setTitle(activity.getString(R.string.delete))
                        .setMessage(activity.getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton(activity.getString(R.string.ok),R.drawable.ic_delete_popup) { dialogInterface, which ->
                            dialogInterface.dismiss()
                            deleteCamera()
                            activity?.showSuccessToast("Delete successfully")
                            itemArrayList?.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
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
                        deleteCamera()
                        activity?.showSuccessToast("Delete successfully")
                        itemArrayList?.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)

                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()
*/




            }

            itemView.checkBox.setOnClickListener {
                itemArrayList?.get(adapterPosition)!!.isSelected =!itemArrayList?.get(adapterPosition)?.isSelected!!
                itemView.checkBox.isChecked = itemArrayList!![adapterPosition].isSelected
                if (itemArrayList?.get(adapterPosition)?.isSelected!!) {
                    arrayAdminCameraIDPK.add(itemArrayList!![adapterPosition].adminCameraIDPK)
                } else {
                    arrayAdminCameraIDPK.remove(itemArrayList!![adapterPosition].adminCameraIDPK)
                }
            }
        }

        override fun onClick(v: View) {
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun deleteCamera() {
            val data = MyCameraSettingDeleteRequest(arrayAdminCameraIDPK)
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.deleteMultipleCamera(data)
            callApi!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.e(response.body().toString())
                    val data = parseJsonObject(response.body().toString())
                    if (data.getBoolean("ResponseResult")) {
                        arrayAdminCameraIDPK.clear()
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }

    }

}
