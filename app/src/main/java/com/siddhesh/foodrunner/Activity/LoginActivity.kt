package com.siddhesh.foodrunner.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response
import com.siddhesh.foodrunner.R
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var etMobileNo: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etMobileNo = findViewById(R.id.etMobileNo)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)
        sharedPreferences =
            getSharedPreferences( getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("status", false)) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnLogin.setOnClickListener {
            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", etMobileNo.text.toString())
            jsonParams.put("password", etPassword.text.toString())
            val jsonRequest =
                object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                        try {
                            println("Response is $it")
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if (success) {
                                sharedPreferences.edit().putBoolean("status", success).apply()
                                val jsonObjectRequest = data.getJSONObject("data")
                                sharedPreferences.edit().putString(
                                    "user_id",
                                    jsonObjectRequest.getString("user_id")
                                ).apply()
                                sharedPreferences.edit().putString(
                                    "name",
                                    jsonObjectRequest.getString("name")
                                ).apply()
                                sharedPreferences.edit()
                                    .putString(
                                        "email",
                                        jsonObjectRequest.getString("email")
                                    ).apply()
                                sharedPreferences.edit().putString(
                                    "mobile_number",
                                    jsonObjectRequest.getString("mobile_number")
                                ).apply()
                                sharedPreferences.edit().putString(
                                    "address",
                                    jsonObjectRequest.getString("address")
                                ).apply()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Invalid User Credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e1: JSONException) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Some Unexpected Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@LoginActivity,
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

        txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}