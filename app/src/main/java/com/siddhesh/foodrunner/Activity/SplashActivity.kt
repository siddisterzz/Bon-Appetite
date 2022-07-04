package com.siddhesh.foodrunner.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.util.ConnectionManager


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ConnectionManager().checkConnectivity(this@SplashActivity)) {
            setContentView(R.layout.activity_splash)
            startAct()
        } else {
            val dailog = AlertDialog.Builder(this@SplashActivity)
            dailog.setTitle("Connection Error")
            dailog.setMessage("Check your connection ")
            dailog.setPositiveButton("Open Settings")
            { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@SplashActivity)
            }
            dailog.create()
            dailog.show()
        }
    }

    fun startAct() {
        Handler().postDelayed({
            val startAct = Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
            startActivity(startAct)
        }, 4800)
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}