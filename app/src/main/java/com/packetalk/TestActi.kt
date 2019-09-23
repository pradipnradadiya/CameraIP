package com.packetalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActi : BaseActivity() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_test
    }

    override fun init() {
        bindToolBar("test")
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
