package com.packetalk.Trailer.fragment.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.packetalk.R
import com.packetalk.Trailer.fragment.activity.TrailerAct
import com.packetalk.Trailer.fragment.model.trailer.Object
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.frg_trailer_item.view.*

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

        fun setData(data: Object?) {
            itemView.tvTrailerGaugeName.text = data!!.trailerName
            itemView.tvSolarType.text = data.type
            if (data.type == "solar") {
                itemView.switchTrailer.isOn = data.isOn
                itemView.switchTrailer.visibility = View.VISIBLE
                itemView.imgRestart.visibility = View.VISIBLE
            } else {
                itemView.switchTrailer.visibility = View.INVISIBLE
                itemView.imgRestart.visibility = View.INVISIBLE
            }
            when {
                data.risk == "Normal" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.INVISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.INVISIBLE
                    itemView.tubeSpeedometer.visibility = View.VISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border)
                }
                data.risk == "Critical" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.INVISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.VISIBLE
                    itemView.tubeSpeedometer.visibility = View.INVISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border_red)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border_red)
                }
                data.risk == "Warning" -> {
                    itemView.tubeSpeedometerYellow.visibility = View.VISIBLE
                    itemView.tubeSpeedometerRed.visibility = View.INVISIBLE
                    itemView.tubeSpeedometer.visibility = View.INVISIBLE
                    itemView.linMainBorder.setBackgroundResource(R.drawable.trailer_border_yellow)
                    itemView.linSubBorder.setBackgroundResource(R.drawable.trailer_border_yellow)
                }
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.switchTrailer.setOnToggledListener { labeledSwitch, isOn ->
                if (isOn) {
                    AppLogger.e("switch on")
                    itemArrayList?.get(adapterPosition)!!.isOn = true
                    notifyItemChanged(adapterPosition)
                } else {
                    AppLogger.e("switch off")
                    itemArrayList?.get(adapterPosition)!!.isOn = false
                    notifyItemChanged(adapterPosition)
                }
            }

            itemView.linSubBorder.setOnClickListener {
                val intent = Intent(activity,TrailerAct::class.java)
                activity?.startActivity(intent)
            }
        }

        override fun onClick(v: View) {
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
