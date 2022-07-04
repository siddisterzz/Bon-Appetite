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

class RegisterActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbar = findViewById(R.id.toolbar)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etAddress = findViewById(R.id.etAddress)
        etMobile = findViewById(R.id.etMobileNo)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setUpToolbar()
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            if (password == confirmPassword) {
                val queue = Volley.newRequestQueue(this@RegisterActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", etName.text.toString())
                jsonParams.put("email", etEmail.text.toString())
                jsonParams.put("mobile_number", etMobile.text.toString())
                jsonParams.put("password", etPassword.text.toString())
                jsonParams.put("address", etAddress.text.toString())
                val jsonRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            println("response is $it")
                            val data = it.getJSONObject("data")
                            println("Response is $it")
                            val success = data.getBoolean("success")
                            if (success) {
                                val RegisterJsonObject = data.getJSONObject("data")
                                sharedPreferences.edit()
                                    .putString(
                                        "user_id",
                                        RegisterJsonObject.getString("user_id")
                                    )
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("name", RegisterJsonObject.getString("name"))
                                    .apply()
                                sharedPreferences.edit()
                                    .putString("email", RegisterJsonObject.getString("email"))
                                    .apply()
                                sharedPreferences.edit().putString(
                                    "mobile_number",
                                    RegisterJsonObject.getString("mobile_number")
                                ).apply()
                                sharedPreferences.edit()
                                    .putString(
                                        "address",
                                        RegisterJsonObject.getString("address")
                                    )
                                    .apply()
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Mobile Number or Email Id is Already Registered",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e1: JSONException) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Some Unexpected Error Occurred 2",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Some Unexpected Error Occurred 1",
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
                    this@RegisterActivity,
                    "Password and Confirm Password must be same",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onBackPressed() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}