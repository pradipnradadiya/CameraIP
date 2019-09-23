package com.packetalk


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.packetalk.home.activity.HomeAct

class TestFragment : BaseFragment() {
    lateinit var rootView: View
    override fun myFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_test, parent, false)
        return rootView
    }

    override fun init() {
        (activity as HomeAct).bindToolBarBack("Add Trailer")
    }

    override fun initView() {
    }

    override fun postInitView() {
    }

    override fun addListener() {
    }

    override fun loadData() {
    }


}
