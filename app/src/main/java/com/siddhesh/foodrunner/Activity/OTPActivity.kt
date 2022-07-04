package com.siddhesh.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import org.json.JSONException
import org.json.JSONObject

class OTPActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnSubmit: Button
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p)
        etOtp = findViewById(R.id.etOtp)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword2)
        btnSubmit = findViewById(R.id.btnSubmit)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val mod = sharedPreferences.getString("mobile_numberF", "")
        btnSubmit.setOnClickListener {
            if (etNewPassword.text.toString() == etConfirmPassword.text.toString()) {
                val queue = Volley.newRequestQueue(this@OTPActivity)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", sharedPreferences.getString("mobile_numberF", ""))
                jsonParams.put("password", etNewPassword.text.toString())
                jsonParams.put("otp", etOtp.text.toString())
                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            println("response is $it")
                            val success = data.getBoolean("success")
                            val successMessage = data.getString("successMessage")
                            if (success) {
                                Toast.makeText(
                                    this@OTPActivity,
                                    successMessage,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val intent = Intent(this@OTPActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                val errorMessage = data.getString("errorMessage")
                                Toast.makeText(
                                    this@OTPActivity,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e1: JSONException) {
                            Toast.makeText(
                                this@OTPActivity,
                                "Type a correct OTP or try again after 24 hours",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@OTPActivity,
                                "Some error occurred",
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
            } else {
                Toast.makeText(
                    this@OTPActivity,
                    "Confirm Password and New Password must be same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@OTPActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}