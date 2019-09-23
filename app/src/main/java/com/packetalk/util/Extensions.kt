package com.packetalk.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.packetalk.R
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject

fun Activity.showToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    val view1 = toast.view
    toast.view.setPadding(20, 20, 20, 20)
    view1.setBackgroundResource(R.color.green)
    //        view1.setTextColor(Color.WHITE);
    toast.show()
}

fun Activity.showSuccessToast(message: String) {
    Toasty.success(this, message, Toast.LENGTH_LONG, true).show()
}

fun Activity.showErrorToast(message: String) {
    Toasty.error(this, message, Toast.LENGTH_LONG, true).show()
}

fun Activity.showInfoToast(message: String) {
    Toasty.info(this, message, Toast.LENGTH_LONG, true).show()
}

fun Activity.showWarningToast(message: String) {
    Toasty.warning(this, message, Toast.LENGTH_LONG, true).show()
}

val Context.isConnected: Boolean
    get() {
        return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo?.isConnected == true
    }


fun parseJson(data: String): String {
    val d = JSONObject(data)
    val json = d.get("d")
    AppLogger.e("json-------------$json")
    val obj = JSONObject(json.toString())
    return obj.toString()
}

fun parseJsonObject(data: String): JSONObject {
    return JSONObject(data)
}

fun parseJsonArray(data: String): String {
    val d = JSONObject(data)
    val json = d.get("d")
    AppLogger.e("json-------------$json")
    val obj = JSONArray(json)
    AppLogger.e("obj-------------$obj")
    return obj.toString()

}

fun Activity.getDisplayWidth(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels / 2
}

fun Activity.getDisplayHeight(): Int {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun setLoader(): AbstractDraweeController<*, *>? {
    //progress loader
    val imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.raw.spinner_icon).build()
    val controller = Fresco.newDraweeControllerBuilder()
        .setUri(imageRequest.sourceUri)
        .setAutoPlayAnimations(true)
        .build()
    return controller
}

