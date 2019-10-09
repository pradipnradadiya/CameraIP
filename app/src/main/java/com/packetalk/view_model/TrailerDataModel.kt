package com.packetalk.view_model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrailerDataModel : ViewModel() {
    //this is the data that we will fetch asynchronously
    private var heroList: MutableLiveData<TrailerGaugeItem>? = null

    //we will call this method to get the data
    fun getTrailer(): LiveData<TrailerGaugeItem> {
        //if the list is null
        if (heroList == null) {
            heroList = MutableLiveData()
            //we will load it asynchronously from server in this method
            getTrailerList()
        }
        //finally we will return the list
        return heroList as MutableLiveData<TrailerGaugeItem>
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("model", "------------cleared")
    }

    private fun getTrailerList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getTrailerGaugeList()
        callApi?.enqueue(object : Callback<TrailerGaugeItem> {

            override fun onResponse(
                call: Call<TrailerGaugeItem>,
                response: Response<TrailerGaugeItem>
            ) {
                AppLogger.response("model response--------" + response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        heroList?.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<TrailerGaugeItem>, t: Throwable) {
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