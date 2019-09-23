package com.packetalk.user.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.user.adapter.LogAdapter
import com.packetalk.user.model.log.LogItem
import com.packetalk.user.model.log.Object
import com.packetalk.util.AppLogger
import com.packetalk.util.setLoader
import com.packetalk.util.showErrorToast
import com.packetalk.util.showSuccessToast
import kotlinx.android.synthetic.main.frg_user_log.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserLogFrg : BaseFragment(), Filterable {
    lateinit var rootView: View
    var logArr: ArrayList<Object>? = null
    private var list: ArrayList<Object>? = null
    var layoutManager: LinearLayoutManager? = null
    var adapter: LogAdapter? = null
    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_user_log, parent, false)
        return rootView
    }

    override fun init() {
        layoutManager = LinearLayoutManager(activity)
    }

    override fun initView() {
        rootView.loader.controller = setLoader()
    }

    override fun postInitView() {
    }

    override fun addListener() {
        rootView.btnClearLog.setOnClickListener {
            clearLog()
        }

        rootView.edSearchLog.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter.filter(s)
            }

        })
    }

    override fun loadData() {
        getLogList()
    }

    private fun clearLog() {
        showProgressDialog("Log", "please wait log is clear..")
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.clearLog()

        callApi!!.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                AppLogger.response(response.body().toString())
                hideProgressDialog()
                if (response.isSuccessful) {
                    val str = JSONObject(response.body().toString())
                    if (str.getBoolean("ResponseResult")) {
                        activity?.showSuccessToast("Success")
                        getLogList()

                    } else {
                        activity?.showErrorToast("Error")
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                hideProgressDialog()
                AppLogger.error(t.message.toString())
            }
        })
    }

    private fun getLogList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getLogList()
        callApi!!.enqueue(object : Callback<LogItem> {
            override fun onResponse(call: Call<LogItem>, response: Response<LogItem>) {
                AppLogger.e(response.body().toString())

                if (response.isSuccessful) {
                    rootView.loader.visibility = View.INVISIBLE
                    if (response.body()?.responseResult!!) {

                        logArr = response.body()?.objectX
                        rootView.recycleViewUsersLogs.layoutManager = layoutManager
                        adapter = LogAdapter(activity, response.body()?.objectX)
                        rootView.recycleViewUsersLogs.adapter = adapter
                    }

                }
            }

            override fun onFailure(call: Call<LogItem>, t: Throwable) {
                hideProgressDialog()
            }
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var queryString = constraint
                val results = FilterResults()
                if (queryString == null || queryString.isEmpty()) { // if your editText field is empty, return full list of FriendItem
                    results.count = logArr!!.size
                    results.values = logArr
                } else {
                    val filteredList = ArrayList<Object>()
                    queryString = queryString.toString().toLowerCase() // if we ignore case
                    for (item in logArr.orEmpty()) {
                        val id: String = item.iD.toString() // if we ignore case
                        val desc = item.desc.toLowerCase() // if we ignore case
                        val action = item.action.toLowerCase() // if we ignore case
                        if (id.contains(queryString.toString()) || action.contains(queryString.toString()) || desc.contains(
                                queryString.toString()
                            )
                        ) {
                            filteredList.add(item) // added item witch contains our text in EditText
                        }
                    }
                    results.count = filteredList.size // set count of filtered list
                    results.values = filteredList // set filtered list
                }
                return results // return our filtered list
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                list = results.values as ArrayList<Object>?
                AppLogger.e(list.toString())
                rootView.recycleViewUsersLogs.layoutManager = layoutManager
                adapter = LogAdapter(activity, list)
                rootView.recycleViewUsersLogs.adapter = adapter
            }
        }
    }
}
