package com.packetalk.home.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.model.group_camera_model.strored_video.Object
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.dialog_calender_from_to_camera_list_item.view.*


class StoredVideoListAdapter(
    private val activity: FragmentActivity?,
    private var itemArrayList: ArrayList<Object>?
) :
    RecyclerView.Adapter<StoredVideoListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.dialog_calender_from_to_camera_list_item, viewGroup, false)
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

    fun setBoldSpannable(myText: String, length: Int): SpannableString {
        val spannableContent = SpannableString(myText)
        spannableContent.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        return spannableContent
    }

    fun setBoldOnKeywords(
        text: String,
        searchKeywords: Array<String>
    ): SpannableStringBuilder {
        val span = SpannableStringBuilder(text)

        for (keyword in searchKeywords) {

            var offset = 0
            var start: Int
            val len = keyword.length

            start = text.indexOf(keyword, offset, true)
            while (start >= 0) {
                val spanStyle = StyleSpan(Typeface.BOLD)
                span.setSpan(spanStyle, start, start + len, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

                offset = start + len
                start = text.indexOf(keyword, offset, true)
            }
        }
        return span
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        @SuppressLint("SetTextI18n")
        fun setData(data: Object?) {
            itemView.tvDateTime.text = setBoldSpannable("Date: ${data!!.dateTime}", 5)
            itemView.tvSize.text = setBoldSpannable("Size: ${data.fileSize} mb", 5)
        }
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.imgView.setOnClickListener {

            }
            itemView.imgDownload.setOnClickListener {

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
