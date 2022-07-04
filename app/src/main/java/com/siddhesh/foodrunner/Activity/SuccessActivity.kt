package com.siddhesh.foodrunner.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.siddhesh.foodrunner.R

class SuccessActivity : AppCompatActivity() {
    lateinit var btnOk: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
        btnOk = findViewById(R.id.btnOk)
        btnOk.setOnClickListener {
            val intent = Intent(this@SuccessActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}