package com.packetalk.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.packetalk.R
import com.packetalk.home.activity.CameraDetailAct
import com.packetalk.home.fragment.HomeFrg
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
import com.packetalk.util.getDisplayWidth
import kotlinx.android.synthetic.main.frg_home_item_camera.view.*



class CameraListAdapter(
    private val activity: FragmentActivity?, var homeFrg: HomeFrg,
    private var itemArrayList: ArrayList<CameraDetailsFull>?
) :
    RecyclerView.Adapter<CameraListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.frg_home_item_camera, viewGroup, false)
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
            activity?.let {
                val width = activity.getDisplayWidth() - 40
                var widths = itemView.fr.width
                AppLogger.e(widths.toString())
                AppLogger.e(width.toString())

                val configuration = activity.getResources().getConfiguration()
                val screenWidthDp = configuration.screenWidthDp - 40
                val url: String =
                    "${AppConstants.HTTP_BIND}${data?.serverURL}:${data?.serverPort}/viewpanel/${data?.cameraIDOnServer}/viewpanel&imagewidth=$screenWidthDp&imageheight=200"
                /* itemView.webCamera.load("http://willingboro1.packetalk.net:5350/viewpanel/4/viewpanel&imagewidth=$width&imageheight=200",
                     it
                 )*/

                AppLogger.e(url)
//                val initialScale = this.getScale(320.0)
//                itemView.webCamera.setInitialScale(initialScale)



                itemView.webCamera.load(
                    url,
                    it
                )
            }
            itemView.tvCameraName.text = data!!.cameraName
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.tvCameraName.setOnClickListener {
                val cameraItem: CameraDetailsFull? = itemArrayList?.get(adapterPosition)
                val intent = Intent(activity, CameraDetailAct::class.java)
                intent.putExtra(AppConstants.CAMERA_DETAIL_KEY, cameraItem)
                activity?.startActivity(intent)
            }
        }

        override fun onClick(v: View) {
            AppLogger.e("on click")
//            val intent = Intent(activity,CameraDetailAct::class.java)
//            activity?.startActivity(intent)
        }

        override fun onLongClick(v: View): Boolean {
            return false
        }

        protected fun getScale(contentWidth: Double?): Int {
            if (activity != null) {
                val displaymetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
                val `val` = displaymetrics.widthPixels / contentWidth!! * 100.0
                return `val`.toInt()
            } else {
                return 100
            }
        }


    }

}
