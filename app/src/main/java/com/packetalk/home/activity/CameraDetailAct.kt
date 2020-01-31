package com.packetalk.home.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
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
    private var dateTime = ""
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mSecond: Int = 0
    private var mMilliSecond: Int = 0
    private var timeFlag: Int = 0
    private lateinit var direction: String
    var zoomCount: Int = 0
    lateinit var dialog: Dialog
    private var startFullDateTime: String = ""
    private var endFullDateTime: String = ""
    private var fullUrlCalender: String = ""

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.linTrailer -> {
                HomeAct.currentMapTrailerFlag = 1
                finish()
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
                AppLogger.e("${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree")
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }

            R.id.imgBottom -> {
                direction = "d"
                AppLogger.e("${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree")
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }

            R.id.imgLeft -> {
                direction = "l"
                AppLogger.e("${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree")
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }

            R.id.imgRight -> {
                direction = "r"
                AppLogger.e("${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree")
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=$direction&dist=5&unit=degree",
                    this@CameraDetailAct
                )
            }

            R.id.linearAutoFocus -> {
                AppLogger.e("${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=video&function=focus&focusvalue=0")
                webHidden.load(
                    "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}/control/${cameraDetail?.cameraIDOnServer}/cmd=video&function=focus&focusvalue=0",
                    this@CameraDetailAct
                )
            }

        }
    }

    var cameraDetail: CameraDetailsFull? = null

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
//        val width = getDisplayWidth() - 16
        val height = (getDisplayHeight() / 2) - 356
        AppLogger.e("height $height")
        val configuration = resources.configuration
        val screenWidthDp = configuration.screenWidthDp - 16
        val screenHeightDp = (configuration.screenHeightDp / 2) - 44
        val url =
            "${AppConstants.HTTP_BIND}${cameraDetail?.serverURL}:${cameraDetail?.serverPort}${AppConstants.VIEW_PANEL}${cameraDetail?.cameraIDOnServer}${AppConstants.VIEW_PANEL_FIRSTSLASH}${AppConstants.AND_IMAGE_WIDTH}$screenWidthDp${AppConstants.AND_IMAGE_HEIGHT}$screenHeightDp"

        AppLogger.e("url $url")
        webFull.setBackgroundColor(Color.parseColor("#000000"))
        webFull.load(url, this@CameraDetailAct)
        tvCameraName.text = cameraDetail?.cameraName ?: "null"
    }

    override fun addListener() {
        linTrailer.setOnClickListener(this)
        linCalender.setOnClickListener(this)
        imgPlus.setOnClickListener(this)
        imgMinus.setOnClickListener(this)
        imgTop.setOnClickListener(this)
        imgBottom.setOnClickListener(this)
        imgLeft.setOnClickListener(this)
        imgRight.setOnClickListener(this)
//        imgAutoFocus.setOnClickListener(this)
        linearAutoFocus.setOnClickListener(this)
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
        AppLogger.e("http://willingboro1.packetalk.net:5350/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=l&dist=$zoomCount&unit=degree")
        webHidden.load(
            "http://willingboro1.packetalk.net:5350/control/${cameraDetail?.cameraIDOnServer}/cmd=dir&dir=l&dist=$zoomCount&unit=degree",
            this@CameraDetailAct
        )
    }

    @SuppressLint("CheckResult")
    override fun loadData() {
//        postInitView()
//        var url = "https://upload.wikimedia.org/wikipedia/en/e/ed/Nyan_cat_250px_frame.PNG"


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
            dialog.loader.visible()
            when {
                dialog.tvFromDate.text.isNullOrBlank() -> {
                    showErrorToast(getString(R.string.select_from_date_time))
                }
                dialog.tvToDate.text.isNullOrBlank() -> {
                    showErrorToast(getString(R.string.select_to_date_time))
                }
                else -> {
                    dialog.loader.controller = setLoader()
                    getDownloadVideo()
                }
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
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                //                dateTime = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                dateTime = "${year}/${monthOfYear + 1}/$dayOfMonth"

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
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                if (timeFlag == 0) {
                    dialog.tvFromDate.text = "$dateTime | $hourOfDay:$minute"
                    startFullDateTime =
                        "$startFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"

//                    tvFromTime.text = "$hourOfDay:$minute"
//                    tvFromDate.text = dateTime
                } else if (timeFlag == 1) {
                    dialog.tvToDate.text = "$dateTime | $hourOfDay:$minute"
                    endFullDateTime = "$endFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"

//                    tvToTime.text = "$hourOfDay:$minute"
//                    tvToDate.text = dateTime
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun getDownloadVideo() {
//        dialog.tvCamNameDateTime.visible()
        dialog.relMain.visible()

        dialog.tvCamNameDateTime.text =
            "${cameraDetail?.cameraName} Time: ${dialog.tvFromDate.text} - ${dialog.tvToDate.text}"
        AppLogger.e(startFullDateTime)
        AppLogger.e(endFullDateTime)

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
                dialog.loader.invisible()
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        val layoutManager = LinearLayoutManager(this@CameraDetailAct)
                        val divider = SimpleDividerItemDecoration(resources)
                        dialog.recycleViewStoredVideo.addItemDecoration(divider)
                        dialog.recycleViewStoredVideo.layoutManager = layoutManager

                        val adapter =
                            StoredVideoListAdapter(this@CameraDetailAct, response.body()?.objectX)
                        dialog.recycleViewStoredVideo.adapter = adapter
                        dialog.recycleViewStoredVideo.visible()
                    }
                }
            }

            override fun onFailure(call: Call<StoredVideoItem>, t: Throwable) {
                dialog.loader.invisible()
            }

        })
    }


}

