package com.packetalk.home.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.home.adapter.StoredVideoListAdapter
import com.packetalk.home.model.group_camera_model.CameraDetailsFull
import com.packetalk.home.model.group_camera_model.strored_video.StoredVideoItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.*
import kotlinx.android.synthetic.main.act_camera_detail.*
import kotlinx.android.synthetic.main.dialog_calender_from_to_camera_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CameraDetailAct : BaseActivity(), View.OnClickListener {
    private var date_time = ""
    var mYear: Int = 0
    var mMonth: Int = 0
    var mDay: Int = 0
    var mHour: Int = 0
    var mMinute: Int = 0
    var mSecond: Int = 0
    var mMilliSecond: Int = 0
    var timeFlag: Int = 0
    lateinit var direction: String
    var zoomCount: Int = 0
    lateinit var dialog: Dialog

    private var startFullDateTime: String = ""
    private var endFullDateTime: String = ""
    private var fullUrlCalender: String = ""


    override fun onClick(v: View?) {
        when (v?.id) {
            /*R.id.linSelectDate -> {
                timeFlag = 0
                datePicker()
            }
            R.id.linToDate -> {
                timeFlag = 1
                datePicker()
            }*/
            R.id.linTrailer -> {

            }
            R.id.linCalender -> {
                showDialog()
            }
            R.id.imgPlus -> {
                zoomCount += 1
                seekBarZoom.progress = zoomCount
                loadZoom()
            }
            R.id.imgMinus -> {
                if (zoomCount != 0) {
                    zoomCount -= 1
                }
                seekBarZoom.progress = zoomCount
                loadZoom()
            }
            R.id.imgTop -> {
                direction = "u"
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }
            R.id.imgBottom -> {
                direction = "d"
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }
            R.id.imgLeft -> {
                direction = "l"
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }
            R.id.imgRight -> {
                direction = "r"
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }
            R.id.imgAutoFocus -> {

            }
        }
    }

    private var cameraDetail: CameraDetailsFull? = null

    override fun getLayoutResourceId(): Int {
        return R.layout.act_camera_detail
    }

    override fun init() {
        bindToolBarBack("Video Detail")
    }

    override fun initView() {
        dialog = Dialog(this@CameraDetailAct)
        cameraDetail = intent.extras?.get(AppConstants.CAMERA_DETAIL_KEY) as CameraDetailsFull
    }

    override fun postInitView() {
        val width = getDisplayWidth() - 16
        val height = (getDisplayHeight() / 2) - 356
        AppLogger.e("height $height")
        val url =
            "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/viewpanel/${cameraDetail?.cameraIDOnServer}/viewpanel&imagewidth=$width&imageheight=$height"
        webFull.load(url, this)
        tvCameraName.text = cameraDetail?.cameraName ?: "null"
    }

    override fun addListener() {
//        linSelectDate.setOnClickListener(this)
//        linToDate.setOnClickListener(this)
        linTrailer.setOnClickListener(this)
        linCalender.setOnClickListener(this)
        imgPlus.setOnClickListener(this)
        imgMinus.setOnClickListener(this)
        imgTop.setOnClickListener(this)
        imgBottom.setOnClickListener(this)
        imgLeft.setOnClickListener(this)
        imgRight.setOnClickListener(this)
        imgAutoFocus.setOnClickListener(this)
        seekBarZoom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                zoomCount = progress
                loadZoom()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun loadZoom() {
        webHidden.load(
            "http://willingboro1.packetalk.net:5350/control/1/cmd=dir&dir=l&dist=$zoomCount&unit=degree",
            this@CameraDetailAct
        )
    }

    override fun loadData() {

    }

    private fun showDialog() {

//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_calender_from_to_camera_list)
//        val name = dialog.findViewById(R.id.edGroupNames) as TextInputEditText
//        name.setText(title)


        dialog.constraintFromDate.setOnClickListener {
            timeFlag = 0
            datePicker()
        }

        dialog.constraintToDate.setOnClickListener {
            timeFlag = 1
            datePicker()
        }
        dialog.btnSubmit.setOnClickListener {
            dialog.loader.visibility = View.VISIBLE
            dialog.loader.controller = setLoader()
            if (dialog.tvFromDate.text.isNullOrBlank()) {
                showErrorToast("Please select from date & time.")
            } else if (dialog.tvToDate.text.isNullOrBlank()) {
                showErrorToast("Please select to date & time.")
            } else {
                getDownloadVideo()
            }
        }
        dialog.btnReset.setOnClickListener {
            dialog.tvFromDate.text = ""
            dialog.tvToDate.text = ""
        }

        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }

    private fun datePicker() {
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date_time = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                if (timeFlag == 0) {
                    startFullDateTime = ("$year-${(monthOfYear + 1)}-$dayOfMonth")
                } else {
                    endFullDateTime = ("$year-${(monthOfYear + 1)}-$dayOfMonth")
                }
                //*************Call Time Picker Here ********************
                timePicker()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun timePicker() {
        // Get Current Time
        val c = Calendar.getInstance()
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMinute = c.get(Calendar.MINUTE)
        mSecond = c.get(Calendar.SECOND)
        mMilliSecond = c.get(Calendar.MILLISECOND)

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute

                if (timeFlag == 0) {
                    dialog.tvFromDate.text = "$date_time | $hourOfDay:$minute"
                    startFullDateTime =
                        "$startFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"
//                    tvFromTime.text = "$hourOfDay:$minute"
//                    tvFromDate.text = date_time
                } else if (timeFlag == 1) {
                    dialog.tvToDate.text = "$date_time | $hourOfDay:$minute"
                    endFullDateTime = "$endFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"
//                    tvToTime.text = "$hourOfDay:$minute"
//                    tvToDate.text = date_time
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    private fun getDownloadVideo() {
        dialog.tvCamNameDateTime.visibility = View.VISIBLE
        dialog.tvCamNameDateTime.text =
            "${cameraDetail?.cameraName} Time: ${dialog.tvFromDate.text} - ${dialog.tvToDate.text}"
        AppLogger.e(startFullDateTime.toString())
        AppLogger.e(endFullDateTime.toString())
        fullUrlCalender =
            "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/info/${cameraDetail?.cameraIDOnServer}/getvar&ivccacheinventory&snapshotfiles=false&starttime=$startFullDateTime&endtime=$endFullDateTime"
        AppLogger.e(fullUrlCalender)

        val map = HashMap<String, String>()
        map["FileXMLURL"] = fullUrlCalender

        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.storedVideo(map)
        callApi?.enqueue(object : Callback<StoredVideoItem> {
            override fun onResponse(
                call: Call<StoredVideoItem>,
                response: Response<StoredVideoItem>
            ) {
                AppLogger.response(response.body().toString())
                dialog.loader.visibility = View.INVISIBLE
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        val layoutManager = LinearLayoutManager(this@CameraDetailAct)
                        val divider = SimpleDividerItemDecoration(resources)
                        dialog.recycleViewStoredVideo.addItemDecoration(divider)

                        dialog.recycleViewStoredVideo.layoutManager = layoutManager
                        val adapter =
                            StoredVideoListAdapter(this@CameraDetailAct, response.body()?.objectX)
                        dialog.recycleViewStoredVideo.adapter = adapter
                    }
                }

            }

            override fun onFailure(call: Call<StoredVideoItem>, t: Throwable) {
                dialog.loader.visibility = View.INVISIBLE
            }

        })
    }

}
