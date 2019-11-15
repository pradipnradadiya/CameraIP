package com.packetalk.Trailer.fragment.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.Trailer.fragment.model.trailer.Object
import kotlinx.android.synthetic.main.act_trailer_gauge_item.view.*

class TrailerSubGaugeAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<TrailerSubGaugeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.act_trailer_gauge_item, viewGroup, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val data = itemArrayList?.get(i)
        holder.setData(data)
    }

    override fun getItemCount(): Int {
        return itemArrayList!!.size
//        return 20
    }

    fun removeAt(position: Int) {
        itemArrayList?.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        fun setData(data: Object?) {
//            itemView.tvTrailerGaugeName.text = data!!.trailerName
//            itemView.tvSolarType.text = data.type

            val minspeed = 0F
            val maxsspedd = 120F
           /* val list = ArrayList<Float>()
            list.add(0.0f)
            list.add(13.0f)
            list.add(26.0f)
            list.add(39.0f)
            list.add(52.0f)
            list.add(65.0f)
            list.add(78.0f)
            list.add(91.0f)
            list.add(104.0f)
            list.add(108.0f)
            list.add(120.0f)*/

            // change MAX speed to 320
            itemView.speedViewTrailer.setMinSpeed(minspeed)
            itemView.speedViewTrailer.setMaxSpeed(maxsspedd)
//        speedView.maxGaugeValue = 200.0f
            itemView.speedViewTrailer.setGaugeMaxSpeed(maxsspedd)
            itemView.speedViewTrailer.speedTextColor
//            itemView.speedViewTrailer.setTicks(10f)

//            itemView.speedViewTrailer.setTicks(list)

            // change speed to 140 Km/h
            itemView.speedViewTrailer.speedTo(50f)
            itemView.speedViewTrailer.cancelSpeedAnimator()
            itemView.speedViewTrailer.setLowSpeedPercent(8.33f)

            itemView.speedViewTrailer.setLowSpeedColor(Color.MAGENTA)

            itemView.speedViewTrailer.setMediumSpeedPercent(12.5f)

            itemView.speedViewTrailer.setAverageSpeedPercent(12.5f)

            itemView.speedViewTrailer.setAverageHighSpeedPercent(12.5f)

        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.linSubBorder.setOnClickListener {
            }
        }

        override fun onClick(v: View) {
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }
    }

}
