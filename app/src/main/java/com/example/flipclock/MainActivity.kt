package com.example.flipclock

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var hourTens: FlipDigit
    private lateinit var hourOnes: FlipDigit
    private lateinit var minuteTens: FlipDigit
    private lateinit var minuteOnes: FlipDigit

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateTime()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 设置根据传感器自动旋转
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        
        setContentView(R.layout.activity_main)

        // Set dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Enable full screen immersive mode
        enableFullScreen()

        // Initialize views
        hourTens = findViewById(R.id.hour_tens)
        hourOnes = findViewById(R.id.hour_ones)
        minuteTens = findViewById(R.id.minute_tens)
        minuteOnes = findViewById(R.id.minute_ones)

        // Set initial time
        updateTime()

        // Start time update
        handler.post(updateRunnable)
    }

    private fun enableFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            )
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableFullScreen()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        
        // 重新加载布局以适应横竖屏
        setContentView(R.layout.activity_main)
        
        // 重新初始化视图
        hourTens = findViewById(R.id.hour_tens)
        hourOnes = findViewById(R.id.hour_ones)
        minuteTens = findViewById(R.id.minute_tens)
        minuteOnes = findViewById(R.id.minute_ones)
        
        // 更新时间显示
        updateTime()
        
        // 重新启用全屏
        enableFullScreen()
    }

    private fun updateTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        hourTens.setDigit(hour / 10)
        hourOnes.setDigit(hour % 10)
        minuteTens.setDigit(minute / 10)
        minuteOnes.setDigit(minute % 10)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)
    }
}
