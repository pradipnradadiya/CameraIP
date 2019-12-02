package com.packetalk.setting.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.ligl.android.widget.iosdialog.IOSDialog
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.setting.activity.AddTrailerACt
import com.packetalk.setting.model.trailer.Object
import com.packetalk.util.AppLogger
import com.packetalk.util.parseJsonObject
import com.packetalk.util.showSuccessToast
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import kotlinx.android.synthetic.main.act_add_trailer_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrailerAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_add_trailer_item, viewGroup, false)
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

        fun setData(data: Object?) {
            itemView.tvDns.text = data!!.dNS
            itemView.tvIp.text = data.iP
            itemView.tvTrailerName.text = data.trailerName
            itemView.tvCommunity.text = data.community
            itemView.tvOid.text = data.oID
            itemView.tvType.text = data.type
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.btnEdit.setOnClickListener {
                (activity as AddTrailerACt).updateTrailer(
                    itemArrayList!![adapterPosition]
                )
            }
            itemView.btnDelete.setOnClickListener {

                val mBottomSheetDialog =
                    BottomSheetMaterialDialog.Builder(activity as AppCompatActivity)
                        .setTitle("DELETE?")
                        .setMessage(activity.getString(R.string.delete_msg))
                        .setCancelable(false)
                        .setPositiveButton("YES",R.drawable.ic_delete_popup) { dialogInterface, which ->
                            deleteTrailer()
                            removeAt(adapterPosition)
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("NO",R.drawable.ic_close) { dialogInterface, which ->
                            dialogInterface.dismiss()
                        }
                        .build()
                // Show Dialog
                mBottomSheetDialog.show()

               /* IOSDialog.Builder(activity)
                    .setTitle(activity?.getString(R.string.delete))
                    .setMessage(activity?.getString(R.string.delete_msg))
                    .setPositiveButton(
                        activity?.getString(R.string.ok)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        // continue with delete
                        deleteTrailer()
                        removeAt(adapterPosition)
                    }
                    .setNegativeButton(
                        activity?.getString(R.string.cancel)
                    ) { dialog, _ -> dialog.dismiss() }.show()*/



            }
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        //using delete trailer
        private fun deleteTrailer() {
            val map = HashMap<String, String>()
            map["TrailerID"] = itemArrayList?.get(adapterPosition)!!.trailerID.toString()
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.deleteTrailer(map)
            callApi!!.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    AppLogger.e(response.body().toString())
                    val json = parseJsonObject(response.body().toString())
                    if (json.getBoolean("ResponseResult")) {
                        activity?.showSuccessToast("Trailer delete successfully")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    AppLogger.e(t.message.toString())
                }
            })
        }

    }
}
