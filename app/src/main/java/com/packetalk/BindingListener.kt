package com.packetalk

interface BindingListener {
    fun init()
    fun initView()
    fun postInitView()
    fun addListener()
    fun loadData()
}