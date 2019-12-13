package com.packetalk.chart

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.text.Html
import android.util.Log
import android.view.*
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
import com.packetalk.util.AppConstants
import com.packetalk.util.AppLogger
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
    var trailerName: String = ""
    var gaugeType: String = ""
    var chartType: String = "Week"
    private var minValue: Float = 0.0f
    private var maxValue: Float = 0.0f
    var flag = true

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

    /*   private fun nextChartData() {
           val points: MutableList<LineChartView.Point> = ArrayList()
           var x = arrayOf(0,100,200,300,400,500,600,700,800)
           var y = arrayOf(0,11,22,33,44,55,66,77,88)
           for (i in 0..8) {
   //            val y = (Math.random() * MAX_Y).toInt()
               points.add(LineChartView.Point(x[i].toLong(), y[i].toLong()))
           }
           chartView!!.points = points
       }*/

    /* private fun setXAxisValues(): ArrayList<String>? {
         val xVals = ArrayList<String>()
         xVals.add("11-01 13:13")
         xVals.add("12-06 18:20")
         xVals.add("13-07 00:57")
         xVals.add("14-07 07:31")
         xVals.add("15-07 14:05")
         xVals.add("16-08 20:31")
         xVals.add("17-08 03:06")
         xVals.add("18-08 09:40")
         xVals.add("19-09 16:55")
         xVals.add("20-09 23:30")
         xVals.add("21-09 06:04")
         xVals.add("22-10 12:51")
         xVals.add("23-10 19:24")
         xVals.add("24-11 01:58")
         xVals.add("25-12 08:32")
         xVals.add("26-12 21:52")
         xVals.add("27-12 04:26")
         xVals.add("28-13 11:00")
         xVals.add("29-13 17:32")
         xVals.add("30-13 00:07")
         xVals.add("31-13 06:42")
         xVals.add("32-14 13:17")
         xVals.add("33-14 19:53")
         return xVals
     }
 */

    /*private fun setYAxisValues(): ArrayList<Entry>? {
        val yVals = ArrayList<Entry>()
        yVals.add(Entry(60f, 0))
        yVals.add(Entry(48f, 1))
        yVals.add(Entry(70.5f, 2))
        yVals.add(Entry(100f, 3))
        yVals.add(Entry(180.9f, 4))
        yVals.add(Entry(180.9f, 5))
        yVals.add(Entry(180.9f, 6))
        yVals.add(Entry(180.9f, 7))
        yVals.add(Entry(180.9f, 8))
        yVals.add(Entry(180.9f, 9))
        yVals.add(Entry(30.9f, 10))
        yVals.add(Entry(180.9f, 11))
        yVals.add(Entry(180.9f, 12))
        yVals.add(Entry(180.9f, 13))
        yVals.add(Entry(150.9f, 14))
        yVals.add(Entry(50.9f, 15))
        yVals.add(Entry(60.9f, 16))
        yVals.add(Entry(180.9f, 17))
        yVals.add(Entry(180.9f, 18))
        yVals.add(Entry(80.9f, 19))
        yVals.add(Entry(180.9f, 20))
        yVals.add(Entry(180.9f, 21))
        yVals.add(Entry(100.9f, 22))
        return yVals
    }*/

    private fun setData(xVal: ArrayList<String>,yVal: ArrayList<Entry>) {
//        val xVals = setXAxisValues()
//        val yVals = setYAxisValues()
        val set1: LineDataSet
        // create a dataset and give it a type
        set1 = LineDataSet(yVal, "DataSet 1")
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
        set1.label = "LINE CHART"
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1) // add the datasets
        // create a data object with the datasets
        val data = LineData(xVal, dataSets)
        // set data
        mChart!!.data = data
    }

    override fun initView() {
        bindToolBarBack("JCPD (FUEL_LEVEL)")
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

        val data = ViewModelProviders.of(this@ChartAct).get(ChartDataModel::class.java)
        data.callGetChartData(trailerName, chartType, startFullDateTime, "", gaugeType, "false")
            .observe(this,
                Observer {

                    val yVal = ArrayList<Entry>()
                    val xVal = it.objectX.lable
                    val yArr: ArrayList<Float> = it.objectX.value
                    for ((index, value) in yArr.withIndex()) {
                        yVal.add(Entry(value.toFloat(), index))
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
                            tvAverage.text = "Avg is: ${it.objectX.averageStatus.status}"
                            tvAverage.setTextColor(resources.getColor(R.color.green))
                        }
                        "Critical" -> {
                            constraintAverage.setBackgroundResource(R.drawable.top_border_red)
                            tvAverage.text = "Avg is: ${it.objectX.averageStatus.status}"
                            tvAverage.setTextColor(resources.getColor(R.color.red))
                        }
                        "Warning" -> {
                            constraintAverage.setBackgroundResource(R.drawable.top_border_yellow)
                            tvAverage.text = "Avg is: ${it.objectX.averageStatus.status}"
                            tvAverage.setTextColor(resources.getColor(R.color.yellow))
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

    override fun onChartGestureEnd(me: MotionEvent?,lastPerformedGesture: ChartTouchListener.ChartGesture?) {
    }

    override fun onChartFling(me1: MotionEvent?,me2: MotionEvent?,velocityX: Float,velocityY: Float) {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartGestureStart(me: MotionEvent?,lastPerformedGesture: ChartTouchListener.ChartGesture?) {
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
                dateTime = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
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
                    edFromDate.setText("$dateTime | $hourOfDay:$minute")
                    startFullDateTime =
                        "$startFullDateTime-$hourOfDay-$minute-$mSecond-$mMilliSecond"
                } else if (timeFlag == 1) {
                    edToDate.setText("$dateTime | $hourOfDay:$minute")
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
                    linDate.visibility = View.GONE
                } else {
                    linDate.visibility = View.VISIBLE
                }
            }

            R.id.action_user ->{

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

            R.id.imgExcel ->{

            }
            R.id.btnGo ->{

            }

        }
    }
}
