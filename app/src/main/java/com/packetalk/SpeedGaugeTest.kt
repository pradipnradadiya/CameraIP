package com.packetalk

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class SpeedGaugeTest : AppCompatActivity() {

    var minspeed = -20F
    var maxsspedd = 180F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_gauge_test)
//        val speedView = findViewById(R.id.speedView) as SpeedView

        var list = ArrayList<Float>()
        list.add(-20.0f)
        list.add(0.0f)
        list.add(20.0f)
        list.add(40.0f)
        list.add(60.0f)
        list.add(80.0f)
        list.add(100.0f)
        list.add(120.0f)
        list.add(140.0f)
        list.add(160.0f)
        list.add(180.0f)

      /*  // change MAX speed to 320
        speedView.setMinSpeed(minspeed)
        speedView.setMaxSpeed(maxsspedd)
//        speedView.maxGaugeValue = 200.0f
        speedView.setGaugeMaxSpeed(maxsspedd)
        speedView.setTicks(list)

        // change speed to 140 Km/h
        speedView.speedTo(50f)
        speedView.setLowSpeedPercent(6.15f)
        speedView.setLowSpeedColor(Color.MAGENTA)
        speedView.setMediumSpeedPercent(15.2f)
        speedView.setAverageSpeedPercent(35.0f)
        speedView.setAverageHighSpeedPercent(46.0f)
*/
    }
}
