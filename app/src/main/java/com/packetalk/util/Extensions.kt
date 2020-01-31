package com.packetalk.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.AbstractDraweeController
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.android.material.snackbar.Snackbar
import com.kaopiz.kprogresshud.KProgressHUD
import com.packetalk.R
import es.dmoral.toasty.Toasty
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern
private var hud: KProgressHUD? = null

fun Activity.showPDialog(title: String, message: String) {
    hud = KProgressHUD(this)
    hud?.dismiss()
    hud = KProgressHUD.create(this)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(title)
        .setDetailsLabel(message)
        .show()
}

fun Activity.hidePDialog() {
    if (hud != null && hud!!.isShowing) {
        hud!!.dismiss()
        hud = null
    }
}



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
    return Fresco.newDraweeControllerBuilder()
        .setUri(imageRequest.sourceUri)
        .setAutoPlayAnimations(true)
        .build()
}

fun ImageView.loadUrl(url: String) {
//    Picasso.with(context).load(url).into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Activity.screenWidth(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}

fun Activity.screenHeight(): Int {
    val metrics: DisplayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    return metrics.heightPixels
}

fun Context.inflate(res: Int, parent: ViewGroup? = null): View {
    return LayoutInflater.from(this).inflate(res, parent, false)
}

inline fun Dialog.ifIsShowing(body: Dialog.() -> Unit) {
    if (isShowing) {
        body()
    }
}

inline fun Snackbar.ifIsShowing(body: Snackbar.() -> Unit) {
    if (isShown) {
        body()
    }
}

// used for simple start activity without Intent parameters
fun Activity.callTo(clazz: Class<out Activity>) {
    startActivity(Intent(this, clazz))
}

const val EMAIL_PATTERN =
    "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"

// used for validate if the current String is an email
fun String.isValidEmail(): Boolean {
    val pattern = Pattern.compile(EMAIL_PATTERN)
    return pattern.matcher(this).matches()
}

// used for show a toast message in the UI Thread
fun Activity.toast(message: String) {
    runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}

@SuppressLint("DefaultLocale")
fun String.upperCaseFirstLetter(): String {
    return this.substring(0, 1).toUpperCase().plus(this.substring(1))
}


fun String.removeFirstLastChar(): String = this.substring(1, this.length - 1)

fun Activity.showLoader(title: String, message: String) {
    KProgressHUD.create(this)
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setLabel(title)
        .setDetailsLabel(message)
        .setCancellable(true)
        .setAnimationSpeed(2)
        .setDimAmount(0.5f)
        .show()
}

fun Activity.hideLoader() {
    KProgressHUD.create(this).dismiss()
}


