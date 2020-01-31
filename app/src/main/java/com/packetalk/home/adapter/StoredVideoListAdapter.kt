package com.packetalk.home.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.CountDownTimer
import android.os.Environment
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.model.group_camera_model.strored_video.Object
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import kotlinx.android.synthetic.main.dialog_calender_from_to_camera_list_item.view.*
import kotlinx.android.synthetic.main.dialog_view_stored_video.*
import java.util.*
import java.util.concurrent.TimeUnit


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
                val configuration = activity?.resources?.configuration
                val screenWidthDp = configuration!!.screenWidthDp - 72

                var sessionId = gen()
                val url =
                    "${AppConstants.HTTP_BIND}${(activity as CameraDetailAct).cameraDetail?.serverURL}:${activity.cameraDetail?.serverPort}/storedpanel/${activity.cameraDetail?.cameraIDOnServer}/storedpanel&folders=${itemArrayList?.get(
                        adapterPosition
                    )?.folderName}&files=${itemArrayList?.get(adapterPosition)?.fileName}&sessionIds=${sessionId}&useivcplayer=false&imagewidth=${screenWidthDp}&imageheight=242"
                AppLogger.e("url------------------" + url)
                // showDialog("http://willingboro1.packetalk.net:5350/storedpanel/${itemArrayList?.get(adapterPosition)?.duration}/storedpanel&folders=${itemArrayList?.get(adapterPosition)?.folderName}&files=${itemArrayList?.get(adapterPosition)?.fileName}&sessionIds=${UUID.randomUUID().toString().substring(0,12)}&useivcplayer=false&imagewidth=${screenWidthDp}&imageheight=242")
                showDialog(url, itemArrayList?.get(adapterPosition)!!, sessionId)

            }

            itemView.imgDownload.setOnClickListener {
                //                val url ="http://willingboro1.packetalk.net:5350/retrieve/${itemArrayList?.get(adapterPosition)?.duration}/cmd=downloadVideo&name=${itemArrayList?.get(adapterPosition)?.folderName}/${itemArrayList?.get(adapterPosition)?.fileName}"
                val url =
                    "http://willingboro1.packetalk.net:5350/retrieve/10/cmd=downloadVideo&name=2020-01-15/Willingboro-Trailer4-Cam1-2020-01-15-13-33-24.jpv"


                AppLogger.e("download url------------" + url)
//                activity?.let { it1 ->
//                    RxDownloader(it1).download(url, "stored video", "jpv", true)
//                        .subscribe { path ->
//                            AppLogger.e("path----------" + path)
//                        }
//                }

                downloadVideo("http://willingboro1.packetalk.net:5350/retrieve/10/cmd=downloadVideo&name=2020-01-15/Willingboro-Trailer4-Cam1-2020-01-15-13-33-24.jpv")
//                downloadVideo("http://mirrors.standaloneinstaller.com/video-sample/Panasonic_HDC_TM_700_P_50i.3gp")
            }
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")

        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        private fun gen(): Int {
            val r = Random(System.currentTimeMillis())
            return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000)
        }

        private fun downloadVideo(url: String): Long {
            val output: Long
            val downloadManager: DownloadManager =
                activity?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request =
                DownloadManager.Request(Uri.parse(url))
            request.setTitle("Download")
            request.setDescription("http://willingboro1.packetalk.net:5350/retrieve/10/cmd=downloadVideo&name=2020-01-15/Willingboro-Trailer4-Cam1-2020-01-15-13-33-24.jpv")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(
                activity,
                Environment.DIRECTORY_DOWNLOADS,
                "Willingboro-Trailer4-Cam1-2020-01-15-13-33-24.jpv"
            )
            output = downloadManager.enqueue(request)
            return output
        }

        private fun showDialog(url: String, data: Object, sessionId: Int) {
            AppLogger.e("url-------" + url)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
            val configuration = activity?.resources?.configuration
//            val screenWidthDp = configuration!!.screenWidthDp - 72
            val dialog = activity?.let { Dialog(it) }
            dialog?.setContentView(R.layout.dialog_view_stored_video)

            dialog?.webStoredVideoView?.loadUrl(url)


            var flag = false
            dialog?.tvPlayPause?.setOnClickListener {
                if (flag) {
                    flag = false
                    AppLogger.e("play url-------${AppConstants.HTTP_BIND}${(activity as CameraDetailAct).cameraDetail?.serverURL}:${activity.cameraDetail?.serverPort}/retrieve/${activity.cameraDetail?.cameraIDOnServer}/speed=${"0"}&archiveUserID=${sessionId}&${Date().time}")
                    dialog.webHidden?.loadUrl("${AppConstants.HTTP_BIND}${activity.cameraDetail?.serverURL}:${activity.cameraDetail?.serverPort}/retrieve/${activity.cameraDetail?.cameraIDOnServer}speed=${"0"}&archiveUserID=${sessionId}&${Date().time}")
                    dialog.tvPlayPause?.text = "Pause"

                } else {
                    flag = true
                    AppLogger.e("pause url-------${AppConstants.HTTP_BIND}${(activity as CameraDetailAct).cameraDetail?.serverURL}:${activity.cameraDetail?.serverPort}/retrieve/${activity.cameraDetail?.cameraIDOnServer}/speed=${"1"}&archiveUserID=${sessionId}&${Date().time}")
                    dialog.webHidden?.loadUrl("${AppConstants.HTTP_BIND}${activity.cameraDetail?.serverURL}:${activity.cameraDetail?.serverPort}/retrieve/${activity.cameraDetail?.cameraIDOnServer}speed=${"1"}&archiveUserID=${sessionId}&${Date().time}")
                    dialog.tvPlayPause?.text = "Play"
                }
            }
            dialog?.imgClosePopup?.setOnClickListener {
                dialog.dismiss()
            }

            //dialog?.seekBar!!.max = 600000
            dialog?.seekBar?.progressDrawable?.setColorFilter(
                activity?.resources?.getColor(R.color.colorPrimary)!!,
                PorterDuff.Mode.SRC_IN
            )
            dialog?.seekBar?.thumb?.setColorFilter(
                activity?.resources?.getColor(R.color.colorPrimaryDark)!!,
                PorterDuff.Mode.SRC_IN
            )
            dialog?.seekBar?.max = itemArrayList?.get(adapterPosition)?.duration?.toInt()!! * 60
            val max: Long = (itemArrayList?.get(adapterPosition)?.duration?.toInt()!! * 60).toLong()
            val timer = object : CountDownTimer(600000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
//                    AppLogger.e("millisUntilFinished-----$second")
                    dialog?.seekBar!!.progress = (max - second.toInt()).toInt()
                }

                override fun onFinish() {
                }
            }
            timer.start()


            dialog?.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    // max = progress
                    AppLogger.e("progress-----$progress")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            })

            dialog?.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()


        }

    }
}
