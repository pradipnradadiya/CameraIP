package com.packetalk.view_model.chart


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.packetalk.chart.model.ChartItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChartDataModel : ViewModel() {
    //this is the data that we will fetch asynchronously
    public var chartData: MutableLiveData<ChartItem>? = null

    //we will call this method to get the data
    fun callGetChartData(
        trailerName: String,
        chartType: String,
        fromDate: String,
        toDate: String,
        gaugeType: String,
        isCalender: String
    ): LiveData<ChartItem> {
        //if the list is null
        if (chartData == null) {
            chartData = MutableLiveData()
            //we will load it asynchronously from server in this method
            getChartData(trailerName, chartType, fromDate, toDate, gaugeType, isCalender)
        }
        //finally we will return the list
        return chartData as MutableLiveData<ChartItem>
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("model", "------------cleared")
    }

    private fun getChartData(
        trailerName: String,
        chartType: String,
        fromDate: String,
        toDate: String,
        gaugeType: String,
        isCalender: String
    ) {
        val map = HashMap<String, String>()
        map["TrailerName"] = trailerName
        map["chartType"] = chartType
        map["Fromdate"] = fromDate
        map["Todate"] = toDate
        map["GaugeType"] = gaugeType
        map["IsCalender"] = isCalender
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getChartData(map)
        callApi?.enqueue(object : Callback<ChartItem> {
            override fun onFailure(call: Call<ChartItem>, t: Throwable) {
            }

            override fun onResponse(call: Call<ChartItem>, response: Response<ChartItem>) {
                AppLogger.response(response.body().toString())
                if (response.isSuccessful) {
                    chartData?.value = response.body()
                }
            }
        })
    }
}

