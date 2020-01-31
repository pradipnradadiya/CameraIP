package com.packetalk.chart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.packetalk.BaseActivity
import com.packetalk.R
import com.packetalk.util.*
import com.packetalk.view_model.chart.ChartDataModel
import kotlinx.android.synthetic.main.activity_chart.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChartAct : BaseActivity(), OnChartGestureListener, OnChartValueSelectedListener,
    View.OnClickListener {
    private var dateTime = ""
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0
    private var mSecond: Int = 0
    private var mMilliSecond: Int = 0
    private var timeFlag: Int = 0
    private var startFullDateTime: String = ""
    private var endFullDateTime: String = ""
    private var mChart: LineChart? = null
    private var minValue: Float = 0.0f
    private var maxValue: Float = 0.0f
    var flag = true
    var trailerName: String = ""
    var gaugeType: String = ""
    var chartType: String = "Week"
    private var isCalender = "false"
    private var fromDate: String = ""
    private var toDate: String = ""

    /*  private val MAX_Y = 100.0
      private var chartView: LineChartView? = null*/


    override fun getLayoutResourceId(): Int {
        return R.layout.activity_chart
    }

    override fun init() {
        mChart = findViewById(R.id.linechart)
        trailerName = intent.getStringExtra(AppConstants.TRAILER_NAME)
        gaugeType = intent.getStringExtra(AppConstants.GAUGE_TYPE)

    }

    override fun initView() {
        bindToolBarBack("$trailerName($gaugeType)")
    }

    override fun postInitView() {
    }

    override fun addListener() {
        edFromDate.setOnClickListener(this)
        edToDate.setOnClickListener(this)
        imgExcel.setOnClickListener(this)
        btnGo.setOnClickListener(this)

        edChartType.setOnClickListener {
            PopupMenu(this@ChartAct, edChartType).apply {
                menuInflater.inflate(R.menu.chart_type_menu, menu)
                setOnMenuItemClickListener { item ->
                    edChartType.setText(item.title)
                    chartType = item.title.toString()
                    isCalender = "false"
                    fromDate = ""
                    toDate = ""
                    val data = ViewModelProviders.of(this@ChartAct).get(ChartDataModel::class.java)
                    data.chartData = null
                    flag = true
                    loadData()
                    true
                }
                show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun loadData() {
        getChartData(
            trailerName,
            chartType,
            fromDate,
            toDate,
            gaugeType,
            isCalender
        )

    }

    @SuppressLint("SetTextI18n")
    private fun getChartData(
        trailerName: String,
        chartType: String,
        startFullDateTime: String,
        endFullDateTime: String,
        gaugeType: String,
        isCalender: String
    ) {
        showProgressDialog("Chart Data", "Please wait..")
        val data = ViewModelProviders.of(this@ChartAct).get(ChartDataModel::class.java)
        data.callGetChartData(
            trailerName,
            chartType,
            startFullDateTime,
            endFullDateTime,
            gaugeType,
            isCalender
        )
            .observe(this,
                Observer {
                    AppLogger.e("chart item")
                    hideProgressDialog()

                    if (it != null){
                        val yVal = ArrayList<Entry>()
                        val xVal = it.objectX.lable
                        val yArr: ArrayList<Float> = it.objectX.value
                        for ((index, value) in yArr.withIndex()) {
                            yVal.add(Entry(value, index))
                        }

//                    minValue = yArr.min()!!.toFloat() - 5
//                    maxValue = yArr.max()!!.toFloat() + 5

                        maxValue = Collections.max(yArr)
                        minValue = Collections.min(yArr)

                        setChartData(xVal, yVal)

                        AppLogger.e("" + minValue)
                        AppLogger.e("" + maxValue)

                        tvHeader.text = it.objectX.header
                        val date = it.objectX.headerDate.split("To")
                        tvFromDate.text = Html.fromHtml("${date[0]}")
                        tvToDate.text = Html.fromHtml("<b>To</b>${date[1]}")

                        when (it.objectX.averageStatus.status) {
                            "Normal" -> {
                                constraintAverage.setBackgroundResource(R.drawable.top_border_round)
                                tvAverage.text = "Avg is ${it.objectX.averageStatus.status}"
                                tvAverage.setTextColor(resources.getColor(R.color.green))
                            }
                            "Critical" -> {
                                constraintAverage.setBackgroundResource(R.drawable.top_border_red)
                                tvAverage.text = "Avg is ${it.objectX.averageStatus.status}"
                                tvAverage.setTextColor(resources.getColor(R.color.red))
                            }
                            "Warning" -> {
                                constraintAverage.setBackgroundResource(R.drawable.top_border_yellow)
                                tvAverage.text = "Avg is ${it.objectX.averageStatus.status}"
                                tvAverage.setTextColor(resources.getColor(R.color.yellow))
                            }
                        }
                    }
                })
    }

    private fun refresh() {
        getLayoutResourceId()
        init()
        initView()
        postInitView()
        addListener()
        loadData()
    }

    private fun setData(xVal: ArrayList<String>, yVal: ArrayList<Entry>) {
//        val xVals = setXAxisValues()
//        val yVals = setYAxisValues()
        val set1: LineDataSet
        // create a dataset and give it a type
        set1 = LineDataSet(yVal, "")
        set1.fillAlpha = 50
        // set1.setFillColor(Color.RED);
// set the line to be drawn like this "- - - - - -"
//   set1.enableDashedLine(10f, 5f, 0f);
// set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.color = Color.RED
        set1.setCircleColor(Color.RED)
        set1.lineWidth = 2f
        set1.circleRadius = 5f
        set1.setDrawCircleHole(true)
        set1.valueTextSize = 9f
        set1.setDrawFilled(false)
        set1.label = ""
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the datasets
        // create a data object with the datasets
        val data = LineData(xVal, dataSets)
        // set data
        mChart!!.data = data
    }

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartFling(
        me1: MotionEvent?, me2: MotionEvent?, velocityX: Float,
        velocityY: Float
    ) {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
        Log.i("Gesture", "START, x: " + me!!.x + ", y: " + me.y);
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
    }

    override fun onChartLongPressed(me: MotionEvent?) {
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {

    }

    private fun setChartData(xVal: ArrayList<String>, yVal: ArrayList<Entry>) {
        mChart!!.onChartGestureListener = this
        mChart!!.setOnChartValueSelectedListener(this)
        mChart!!.setDrawGridBackground(false)
        // add data
        // add data
        setData(xVal, yVal)

        // get the legend (only possible after setting data)
        // get the legend (only possible after setting data)
        val l = mChart!!.legend

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        // modify the legend ...
// l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.form = Legend.LegendForm.LINE

        // no description text
        // no description text
        mChart!!.setDescription("")
        mChart!!.setNoDataTextDescription("")

        // enable touch gestures
        // enable touch gestures
        mChart!!.setTouchEnabled(true)

        // enable scaling and dragging
        // enable scaling and dragging
        mChart!!.isDragEnabled = true
        mChart!!.setScaleEnabled(true)

        // mChart.setScaleXEnabled(true);
// mChart.setScaleYEnabled(true);
//        LimitLine upper_limit = new LimitLine(130f, "Upper Limit");
//        upper_limit.setLineWidth(4f);
//        upper_limit.enableDashedLine(10f, 10f, 0f);
//        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        upper_limit.setTextSize(10f);
//        LimitLine lower_limit = new LimitLine(-30f, "Lower Limit");
//        lower_limit.setLineWidth(4f);
//        lower_limit.enableDashedLine(10f, 10f, 0f);
//        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        lower_limit.setTextSize(10f);
        val leftAxis = mChart!!.axisLeft
        leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines

//        leftAxis.addLimitLine(upper_limit);
//        leftAxis.addLimitLine(lower_limit);
        //        leftAxis.addLimitLine(upper_limit);
//        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(maxValue)
        leftAxis.setAxisMinValue(minValue)
        //leftAxis.setYOffset(20f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(5f, 5f, 0f)
        leftAxis.setDrawZeroLine(false)

        // limit lines are drawn behind data (and not on top)
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true)

        mChart!!.axisRight.isEnabled = false
        //mChart.getViewPortHandler().setMaximumScaleY(2f);
//mChart.getViewPortHandler().setMaximumScaleX(2f);
        mChart!!.animateX(2500, Easing.EasingOption.EaseInOutQuart)

        //  don't forget to refresh the drawing
        //  don't forget to refresh the drawing
        mChart!!.invalidate()

        if (flag) {
            flag = false
            refresh()
        }
        /*  chartView = findViewById(R.id.chart_view)
          chartView?.setManualMinY(0)
          nextChartData()*/
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
                dateTime = year.toString() + "/" + (monthOfYear + 1) + "/" + dayOfMonth.toString()

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
                    edFromDate.setText("$dateTime $hourOfDay:$minute")
                    startFullDateTime =
                        "$startFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"
                } else if (timeFlag == 1) {
                    edToDate.setText("$dateTime $hourOfDay:$minute")
                    endFullDateTime = "$endFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"
                }
            }, mHour, mMinute, true
        )
        timePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        super.onCreateOptionsMenu(menu, inflater)
        menuInflater.inflate(R.menu.chart_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_chart -> {
                if (linDate.visibility == View.VISIBLE) {
                    linDate.gone()
                } else {
                    linDate.visible()
                }
            }

            R.id.action_user -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edFromDate -> {
                timeFlag = 0
                datePicker()
            }

            R.id.edToDate -> {
                timeFlag = 1
                datePicker()
            }

            R.id.imgExcel -> {

            }

            R.id.btnGo -> {
                when {
                    TextUtils.isEmpty(edFromDate.text) -> {
                        showErrorToast("Please select From date&time.")
                    }
                    TextUtils.isEmpty(edToDate.text) -> {
                        showErrorToast("Please select To date&time.")
                    }
                    else -> {
                        val data =
                            ViewModelProviders.of(this@ChartAct).get(ChartDataModel::class.java)
                        data.chartData = null
                        flag = true
                        chartType = "calender"
                        isCalender = "true"
                        fromDate = edFromDate.text.toString()
                        toDate = edToDate.text.toString()
                        getChartData(
                            trailerName,
                            chartType,
                            fromDate,
                            toDate,
                            gaugeType,
                            isCalender
                        )
                    }
                }
            }
        }
    }
}
