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
import com.packetalk.BaseFragment
import com.packetalk.R
import com.packetalk.retrofit.APIClientBasicAuth
import com.packetalk.retrofit.ApiInterface
import com.packetalk.user.adapter.UserAdapter
import com.packetalk.user.model.users.Object
import com.packetalk.user.model.users.UserListItem
import com.packetalk.util.*
import kotlinx.android.synthetic.main.frg_create_user.*
import kotlinx.android.synthetic.main.frg_create_user.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUserFrg : BaseFragment(), Filterable {
    lateinit var rootView: View
    var userArr: ArrayList<Object>? = null
    private var list: ArrayList<Object>? = null
    var layoutManager: LinearLayoutManager? = null
    var adapter: UserAdapter? = null

    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.frg_create_user, parent, false)
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
        edSearch.addTextChangedListener(object : TextWatcher{
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
        getUsersList()
    }

    private fun getUsersList() {
        val apiInterface = APIClientBasicAuth.client?.create(ApiInterface::class.java)
        val callApi = apiInterface?.getUserList()

        callApi!!.enqueue(object : Callback<UserListItem> {

            override fun onResponse(call: Call<UserListItem>, response: Response<UserListItem>) {
                AppLogger.e(response.body().toString())
                if (response.isSuccessful) {
                    if (response.body()?.responseResult!!) {
                        rootView.loader.invisible()
                        userArr = response.body()?.objectX
                        rootView.recycleViewCreateUser.layoutManager = layoutManager
                        if (response.body()?.objectX?.isEmpty()!!){
                            rootView.tvNoData.visible()
                            rootView.recycleViewCreateUser.invisible()
                        }else {
                            rootView.tvNoData.invisible()
                            rootView.recycleViewCreateUser.visible()
                            adapter = UserAdapter(activity, response.body()?.objectX)
                            rootView.recycleViewCreateUser.adapter = adapter
                            val divider = SimpleDividerItemDecoration(resources)
                            rootView.recycleViewCreateUser.addItemDecoration(divider)

                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserListItem>, t: Throwable) {
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
                    results.count = userArr!!.size
                    results.values = userArr
                } else {
                    val filteredList = ArrayList<Object>()
                    queryString = queryString.toString().toLowerCase() // if we ignore case
                    for (item in userArr.orEmpty()) {
                        val username = item.username.toLowerCase() // if we ignore case
                        if (username.contains(queryString.toString())
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
                rootView.recycleViewCreateUser.layoutManager = layoutManager
                adapter = UserAdapter(activity, list)
                rootView.recycleViewCreateUser.adapter = adapter
            }
        }
    }

}
