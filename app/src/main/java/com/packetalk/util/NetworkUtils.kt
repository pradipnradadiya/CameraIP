package com.packetalk.util

import android.content.Context
import android.net.ConnectivityManager
import java.net.InetAddress


object NetworkUtils {

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    fun isInternetAvailable(): Boolean {
        return try {
            InetAddress.getByName("google.com")
            return true
        } catch (e: Exception) {
            false
        }
    }


}
