package com.packetalk.splash.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.packetalk.R
import com.packetalk.home.activity.HomeAct
import com.packetalk.login.activity.LoginAct
import com.packetalk.util.SharedPreferenceSession

class SplashACt : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 5000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val session = SharedPreferenceSession(this)
            if (session.userType.isNullOrBlank()){
                val intent = Intent(applicationContext, LoginAct::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, HomeAct::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)
        //Initialize the Handler
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}
