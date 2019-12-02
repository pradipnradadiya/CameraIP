package com.packetalk.view_model.trailer


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.packetalk.Trailer.fragment.model.trailer.TrailerSubGaugeItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerGaugeDataModel : ViewModel() {
    //this is the data that we will fetch asynchronously
    private var gaugeList: MutableLiveData<TrailerSubGaugeItem>? = null

    //we will call this method to get the data
    fun getTrailer(vitalType: String, trailerName: String): LiveData<TrailerSubGaugeItem> {
        //if the list is null
        if (gaugeList == null) {
            gaugeList = MutableLiveData()
            //we will load it asynchronously from server in this method
            getTrailerGaugeList(vitalType, trailerName)
        }
        //finally we will return the list
        return gaugeList as MutableLiveData<TrailerSubGaugeItem>
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("model", "------------cleared")
    }

    private fun getTrailerGaugeList(vitalType: String, trailerName: String) {
        val map = HashMap<String, String>()
        map["VitalType"] = vitalType
        map["TrailerName"] = trailerName
        AppLogger.e("map------------$map")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getSubTrailerGaugeList(map)
        callApi?.enqueue(object : Callback<TrailerSubGaugeItem> {

            override fun onResponse(
                call: Call<TrailerSubGaugeItem>,
                response: Response<TrailerSubGaugeItem>
            ) {
                AppLogger.response("model response--------" + response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        gaugeList?.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<TrailerSubGaugeItem>, t: Throwable) {
                AppLogger.error(t.message.toString())
            }
        })
    }
}


/*  Using of this model in activity or fragment
  val data = ViewModelProviders.of(this@TrailerAct).get(TrailerDataModel::class.java)
        data.getTrailer().observe(this,
            Observer<TrailerGaugeItem> { heroList ->
                AppLogger.e("data is----------"+heroList.objectX.toString())
            })
* */