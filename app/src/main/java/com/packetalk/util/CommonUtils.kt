package com.packetalk.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.packetalk.splash.activity.SplashACt
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val sdf1 = SimpleDateFormat("MM-dd-yyyy HH:mm:ss aa")

    private val ALLOWED_CHARACTERS = AppConstants.RANDOM_STR

    @SuppressLint("DefaultLocale")
    fun upperCaseFirstLetter(str: String): String {
        return str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    fun rupeeFormat(value: String): String {
        var value = value
        value = value.replace(",", "")
        val lastDigit = value[value.length - 1]
        var result = ""
        val len = value.length - 1
        var nDigits = 0

        for (i in len - 1 downTo 0) {
            result = value[i] + result
            nDigits++
            if (nDigits % 2 == 0 && i > 0) {
                result = ",$result"
            }
        }
        return result + lastDigit
    }

    fun priceFormat(amount: Float?): String {
        var price: String
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        price = formatter.format(amount)
        price = price.substring(0, price.length - 3)
        return price
        //        return formatter.format(amount);
    }

    fun logout(from: Activity) {
        val i = Intent(from, SplashACt::class.java)
        from.startActivity(i)
        from.finishAffinity()
    }

    //String encode
    fun encodeEmoji(message: String): String {
        return try {
            URLEncoder.encode(
                message,
                "UTF-8"
            )
        } catch (e: UnsupportedEncodingException) {
            message
        }

    }

    //String decode
    fun decodeEmoji(message: String): String {
        val myString: String? = null
        return try {
            URLDecoder.decode(
                message, "UTF-8"
            )
        } catch (e: UnsupportedEncodingException) {
            message
        }

    }

    fun convertDate(date: String): Date? {
        var mDate: Date? = null
        try {
            mDate = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return mDate
    }

    fun giveDate(time: String): String {
        val cal = Calendar.getInstance()
        return sdf.format(cal.time)
    }

    fun getDate(fullDate: String): String {
//        val dateFormat =
//            SimpleDateFormat("MM/dd/yyyy  hh:mm:ss  aa")

        var date = giveDate(fullDate)

        val d = sdf1.parse(date)
        return d.toString()
    }

    fun getRandomString(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString)
            sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
        return sb.toString()
    }


    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getVersionName(context: Context): String {
        var pInfo: PackageInfo? = null
        try {
            pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return pInfo!!.versionName

    }

    fun setButtonBackgroundColor(context: Context, button: Button, color: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            button.setBackgroundColor(context.resources.getColor(color, null))
        } else {
            button.setBackgroundColor(context.resources.getColor(color))
        }
    }

    fun setButtonTextColor(context: Context, button: Button, color: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            button.setTextColor(context.resources.getColor(color, null))
        } else {
            button.setTextColor(context.resources.getColor(color))
        }
    }

    fun setTextViewTextColor(context: Context, textView: TextView, color: Int) {

        if (Build.VERSION.SDK_INT >= 23) {
            textView.setTextColor(context.resources.getColor(color, null))
        } else {
            textView.setTextColor(context.resources.getColor(color))
        }
    }

    fun setTextViewBackgroundColor(context: Context, textView: TextView, color: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            textView.setBackgroundColor(context.resources.getColor(color, null))
        } else {
            textView.setBackgroundColor(context.resources.getColor(color))
        }
    }

    fun setDrawableSelector(context: Context, normal: Int, selected: Int): Drawable {

        val state_normal = ContextCompat.getDrawable(context, normal)

        val state_pressed = ContextCompat.getDrawable(context, selected)

        val state_normal_bitmap =
            (Objects.requireNonNull<Drawable>(state_normal) as BitmapDrawable).bitmap

        // Setting alpha directly just didn't work, so we draw a new bitmap!
        val disabledBitmap = Bitmap.createBitmap(
            state_normal!!.getIntrinsicWidth(),
            state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(disabledBitmap)

        val paint = Paint()
        paint.alpha = 126
        canvas.drawBitmap(state_normal_bitmap, 0f, 0f, paint)

        val state_normal_drawable = BitmapDrawable(context.resources, disabledBitmap)


        val drawable = StateListDrawable()

        drawable.addState(
            intArrayOf(android.R.attr.state_selected),
            state_pressed
        )
        drawable.addState(
            intArrayOf(android.R.attr.state_enabled),
            state_normal_drawable
        )

        return drawable
    }

    fun selectorRadioImage(
        context: Context,
        normal: Drawable,
        pressed: Drawable
    ): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_checked), pressed)
        states.addState(intArrayOf(), normal)
        //                imageView.setImageDrawable(states);
        return states
    }

    fun selectorRadioButton(context: Context, normal: Int, pressed: Int): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_checked), ColorDrawable(pressed))
        states.addState(intArrayOf(), ColorDrawable(normal))
        return states
    }

    fun selectorRadioText(context: Context, normal: Int, pressed: Int): ColorStateList {
        return ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(pressed, normal)
        )
    }

    fun selectorRadioDrawable(normal: Drawable, pressed: Drawable): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_checked), pressed)
        states.addState(intArrayOf(), normal)
        return states
    }

    fun selectorBackgroundColor(context: Context, normal: Int, pressed: Int): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), ColorDrawable(pressed))
        states.addState(intArrayOf(), ColorDrawable(normal))
        return states
    }

    fun selectorBackgroundDrawable(normal: Drawable, pressed: Drawable): StateListDrawable {
        val states = StateListDrawable()
        states.addState(intArrayOf(android.R.attr.state_pressed), pressed)
        states.addState(intArrayOf(), normal)
        return states
    }

    fun selectorText(context: Context, normal: Int, pressed: Int): ColorStateList {
        return ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf()),
            intArrayOf(pressed, normal)
        )
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    //character capital of white space
    fun capitalizeString(string: String): String {
        val chars = string.toLowerCase().toCharArray()
        var found = false
        for (i in chars.indices) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i])
                found = true
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false
            }
        }
        return String(chars)
    }


    fun isMonthValid(monthString: String): Boolean {
        if (monthString.length != 2) {
            return false;
        }
        val month: Int = Integer.valueOf(monthString);
        if (month >= 1 && month <= 12) {
            return true;
        }
        return false;
    }

}
