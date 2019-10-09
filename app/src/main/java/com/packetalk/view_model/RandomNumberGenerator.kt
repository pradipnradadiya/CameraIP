package com.packetalk.view_model


import android.util.Log
import androidx.lifecycle.ViewModel
import com.packetalk.Trailer.fragment.model.trailer.TrailerGaugeItem
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.util.AppLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RandomNumberGenerator : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private var myRandomNumber: String? = null
    private var check: String? = null

    var responseData = TrailerGaugeItem(null, null, null)

    fun getMyRandomNumber(): String? {
        if (myRandomNumber == null) {
            Log.e("create number", "------------")
            createNumber()
            Log.e("create number", "------------" + myRandomNumber!!)
        }
        return myRandomNumber
    }

    private fun createNumber() {
        val random = Random()
        myRandomNumber = "" + (random.nextInt(100 - 1) + 1)
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("model", "------------cleared")
    }

    fun getTrailerList(): TrailerGaugeItem? {
        if (check == null) {
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
                            responseData = response.body()!!
                            check = response.body()!!.responseResult.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<TrailerGaugeItem>, t: Throwable) {
                    AppLogger.error(t.message.toString())
                }
            })
        }

        return responseData;
    }
}
