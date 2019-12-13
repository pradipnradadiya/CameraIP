package com.packetalk.Trailer.fragment.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.packetalk.R
import com.packetalk.Trailer.fragment.activity.HybridVitalGaugeAct
import com.packetalk.Trailer.fragment.activity.TrailerGaugeAct
import com.packetalk.Trailer.fragment.activity.TrailerGaugeAct2
import com.packetalk.Trailer.fragment.model.trailer.Object
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import com.packetalk.util.SharedPreferenceSession
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog
import kotlinx.android.synthetic.main.frg_trailer_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerGaugeAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<TrailerGaugeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frg_trailer_item, viewGroup, false)
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

        @SuppressLint("DefaultLocale")
        fun setData(data: Object?) {
            itemView.tvTrailerGaugeName.text = data!!.trailerName
            itemView.tvSolarType.text = data.type

            //using for gauge resk status check
            when (data.risk) {
                "Normal" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.INVISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.INVISIBLE
                    itemView.tubeSpeedometer.visibility = View.VISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border)
                }
                "Critical" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.INVISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.VISIBLE
                    itemView.tubeSpeedometer.visibility = View.INVISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border_red)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border_red)
                }
                "Warning" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.VISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.INVISIBLE
                    itemView.tubeSpeedometer.visibility = View.INVISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border_yellow)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border_yellow)
                }
            }

            if (data.type == "solar") {
                itemView.relContent1.visibility = View.VISIBLE
                itemView.relContent2.visibility = View.GONE

                if (data.engineStatus == "Not able to connect it.") {
//                    itemView.imgRestart.isEnabled = false
                    itemView.switchTrailer.isEnabled = false
                    itemView.tvEngineStatus.visibility = View.VISIBLE
                    itemView.viewEngineStatus.visibility = View.VISIBLE

                } else {
//                    itemView.imgRestart.isEnabled = false
                    itemView.switchTrailer.isEnabled = true
                    itemView.switchTrailer.isChecked = data.isOn
                    itemView.tvEngineStatus.visibility = View.INVISIBLE
                    itemView.viewEngineStatus.visibility = View.INVISIBLE
                    if (data.engineStatus == "true") {
                        data.isOn = true
                        itemView.switchTrailer.isChecked = true
//                        notifyItemChanged(adapterPosition)
                    } else {
                        data.isOn = false
                        itemView.switchTrailer.isChecked = true
//                        notifyItemChanged(adapterPosition)
                    }
                }

//                itemView.switchTrailer.isOn = data.isOn
//                itemView.switchTrailer.isChecked = data.isOn
//                itemView.switchTrailer.visibility = View.VISIBLE
//                itemView.imgRestart.visibility = View.VISIBLE

            } else if (data.type == "diesel") {
                itemView.relContent1.visibility = View.GONE
                itemView.relContent2.visibility = View.VISIBLE

                if (data.engineStatus.toLowerCase() == "running" || data.engineStatus.toLowerCase() == "run" || data.engineStatus.toLowerCase() == "auto") {
                    itemView.tvEngineStatus.visibility = View.VISIBLE
                    itemView.viewEngineStatus.visibility = View.VISIBLE
                    itemView.tvEngineStatus.text = data.engineStatus
                    itemView.tvEngineStatus.setTextColor(activity?.resources!!.getColor(R.color.green))
                    itemView.viewEngineStatus.setBackgroundColor(activity.resources!!.getColor(R.color.green))
                } else {
                    itemView.tvEngineStatus.visibility = View.VISIBLE
                    itemView.viewEngineStatus.visibility = View.VISIBLE
                    itemView.tvEngineStatus.text = data.engineStatus
                    itemView.tvEngineStatus.setTextColor(activity?.resources!!.getColor(R.color.red))
                    itemView.viewEngineStatus.setBackgroundColor(activity.resources!!.getColor(R.color.red))
                }

            } else {
                itemView.relContent1.visibility = View.VISIBLE
                itemView.relContent2.visibility = View.GONE

                if (data.engineStatus == "Not able to connect it.") {
//                    itemView.imgRestart.isEnabled = false
                    itemView.switchTrailer.isEnabled = false
                    itemView.tvEngineStatus.visibility = View.VISIBLE
                    itemView.viewEngineStatus.visibility = View.VISIBLE

                } else {
//                    itemView.imgRestart.isEnabled = false
                    itemView.switchTrailer.isEnabled = true
                    itemView.switchTrailer.isChecked = data.isOn
                    itemView.tvEngineStatus.visibility = View.INVISIBLE
                    itemView.viewEngineStatus.visibility = View.INVISIBLE
                    if (data.engineStatus == "true") {
                        data.isOn = true
//                        notifyItemChanged(adapterPosition)
                    } else {
                        data.isOn = false
//                        notifyItemChanged(adapterPosition)
                    }
                }
            }

        }

        init {
            val session = activity?.let { SharedPreferenceSession(it) }
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.switchTrailer.setOnClickListener {
                if (itemArrayList?.get(adapterPosition)!!.isOn) {
                    itemArrayList?.get(adapterPosition)!!.isOn = false
                    notifyItemChanged(adapterPosition)
                    AppLogger.e("off")
                    onOffTrailer(
                        itemArrayList?.get(adapterPosition)!!.trailerName,
                        itemArrayList?.get(adapterPosition)!!.isOn.toString()
                    )
                } else {
                    itemArrayList?.get(adapterPosition)!!.isOn = true
                    notifyItemChanged(adapterPosition)
                    AppLogger.e("on")
                    onOffTrailer(
                        itemArrayList?.get(adapterPosition)!!.trailerName,
                        itemArrayList?.get(adapterPosition)!!.isOn.toString()
                    )
                }
            }

            itemView.imgRestart.setOnClickListener {

                val mBottomSheetDialog =
                    BottomSheetMaterialDialog.Builder(activity as AppCompatActivity)
                        .setTitle("RESTART?")
                        .setMessage("Are you sure to restart Trailer??")
                        .setCancelable(false)
                        .setPositiveButton("OK",R.drawable.ic_refresh) { dialogInterface, which ->
                            onOffTrailer(itemArrayList?.get(adapterPosition)!!.trailerName,"true")
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton("Cancel",R.drawable.ic_close) { dialogInterface, which ->
                            dialogInterface.dismiss()
                        }
                        .build()
                // Show Dialog
                mBottomSheetDialog.show()



            }

            itemView.btnStart.setOnClickListener {
                startDiesel(session?.userName.toString(),itemArrayList?.get(adapterPosition)?.trailerName.toString())
            }

            itemView.btnStop.setOnClickListener {
                stopDiesel(session?.userName.toString(),itemArrayList?.get(adapterPosition)?.trailerName.toString())
            }

            itemView.imgReset.setOnClickListener {
                resetDiesel(session?.userName.toString(),itemArrayList?.get(adapterPosition)?.trailerName.toString())
            }

            itemView.linSubBorder.setOnClickListener {

                if (itemArrayList?.get(adapterPosition)?.type == "diesel") {
                    val intent = Intent(activity, TrailerGaugeAct::class.java)
                    intent.putExtra("vitalType", itemArrayList?.get(adapterPosition)?.type)
                    intent.putExtra("trailerName", itemArrayList?.get(adapterPosition)?.trailerName)
                    activity?.startActivity(intent)
                } else if (itemArrayList?.get(adapterPosition)?.type == "solar") {
                    val intent = Intent(activity, TrailerGaugeAct2::class.java)
                    intent.putExtra("vitalType", itemArrayList?.get(adapterPosition)?.type)
                    intent.putExtra("trailerName", itemArrayList?.get(adapterPosition)?.trailerName)
                    activity?.startActivity(intent)
                } else if (itemArrayList?.get(adapterPosition)?.type == "hybrid") {
                    val intent = Intent(activity, HybridVitalGaugeAct::class.java)
                    intent.putExtra("vitalType", itemArrayList?.get(adapterPosition)?.type)
                    intent.putExtra("trailerName", itemArrayList?.get(adapterPosition)?.trailerName)
                    activity?.startActivity(intent)
                }
            }
        }

        override fun onClick(v: View) {
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun onOffTrailer(trailerName: String, status: String) {
            val map = HashMap<String, String>()
            map["TailerName"] = trailerName
            map["Status"] = status
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.onOffTrailer(map)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        AppLogger.response(response.body().toString())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })
        }

        private fun startDiesel(userName: String, trailerName: String) {
            val map = HashMap<String, String>()
            map["UserName"] = userName
            map["TrailerName"] = trailerName
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.startDiesel(map)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        AppLogger.response(response.body().toString())
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }

        private fun stopDiesel(userName: String, trailerName: String) {
            val map = HashMap<String, String>()
            map["UserName"] = userName
            map["TrailerName"] = trailerName
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.stopDiesel(map)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        AppLogger.response(response.body().toString())
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }

        private fun resetDiesel(userName: String, trailerName: String) {
            val map = HashMap<String, String>()
            map["UserName"] = userName
            map["TrailerName"] = trailerName
            val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
            val callApi = apiInterface?.resetDiesel(map)
            callApi?.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        AppLogger.response(response.body().toString())
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }
            })
        }
    }
}