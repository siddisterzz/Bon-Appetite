package com.siddhesh.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var etMobileNo: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        etMobileNo = findViewById(R.id.etMobileNo)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        btnNext.setOnClickListener {
            sharedPreferences.edit().putString("mobile_numberF", etMobileNo.text.toString()).apply()
            val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNo.text.toString())
            jsonParams.put("email", etEmail.text.toString())
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        println("Response is $it")
                        val success = data.getBoolean("success")
                        if (success) {
                            val firstTry = data.getBoolean("first_try")
                            if (firstTry) {
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "OTP sent successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@ForgotPasswordActivity,
                                    OTPActivity::class.java
                                )
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@ForgotPasswordActivity,
                                    "OTP sent",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(
                                    this@ForgotPasswordActivity,
                                    OTPActivity::class.java
                                )
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Some Unexpected Error Occurred 1",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } catch (e1: JSONException) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Some Unexpected Error Occurred 2",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                    Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Some Unexpected Error Occurred 3",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d9eeb061efbb60"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
