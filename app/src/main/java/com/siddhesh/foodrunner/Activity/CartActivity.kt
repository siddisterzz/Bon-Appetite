package com.siddhesh.foodrunner.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.adapter.CartRecyclerAdapter
import com.siddhesh.foodrunner.modal.RestaurantOrder
import com.siddhesh.foodrunner.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var RecyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: CartRecyclerAdapter
    lateinit var btnPlaceOrder: Button
    lateinit var txtName: TextView
    lateinit var sharedPreferences: SharedPreferences
    val restaurantOrder = arrayListOf<RestaurantOrder>()
    var Name: ArrayList<String>? = arrayListOf<String>()
    var restaurantId: String? = "0"
    var Foodid: ArrayList<String>? = arrayListOf<String>()
    var FoodCost: ArrayList<String>? = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        toolbar = findViewById(R.id.toolbar)
        RecyclerView = findViewById(R.id.RecyclerView)
        txtName = findViewById(R.id.txtName)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        setUpToolbar()
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        if (intent != null) {
            Name = intent.getStringArrayListExtra("name")
            restaurantId = intent.getStringExtra("restaurantId")
            Foodid = intent.getStringArrayListExtra("id")
            FoodCost = intent.getStringArrayListExtra("costForOne")
            txtName.text = intent.getStringExtra("title")
        }
        var sum = 0
        for (i in 0 until (FoodCost!!.size)) {
            val CostEach: Int = FoodCost!![i].toInt()
            sum = sum + CostEach
        }
        btnPlaceOrder.text="Place Order(${getString(R.string.Rs)}$sum)"
        for (i in 0 until FoodCost!!.size) {
            val item = RestaurantOrder(Name!![i], FoodCost!![i])
            restaurantOrder.add(item)
        }
        val foodIdJsonArray = JSONArray()
        val jsonObject = JSONObject()
        for (i in 0 until Foodid!!.size) {
            jsonObject.put("food_item_id", Foodid!![i])
            foodIdJsonArray.put(jsonObject)
        }
        val queue = Volley.newRequestQueue(this@CartActivity)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"
        val jsonParams = JSONObject()
        jsonParams.put("user_id", sharedPreferences.getString("user_id", ""))
        jsonParams.put("restaurant_id", restaurantId)
        jsonParams.put("total_cost", sum)
        jsonParams.put("food", foodIdJsonArray)
        if (ConnectionManager().checkConnectivity(this@CartActivity)) {
            btnPlaceOrder.setOnClickListener {
                val JsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            try {
                                Toast.makeText(
                                    this@CartActivity,
                                    "Order Placed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@CartActivity,
                                    SuccessActivity::class.java
                                )
                                startActivity(intent)
                            } catch (e1: JSONException) {
                                Toast.makeText(
                                    this@CartActivity,
                                    "Some Error Occurred 1",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@CartActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                        Response.ErrorListener {
                            Toast.makeText(
                                this@CartActivity,
                                "Some Unexpected Error Occurred",
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
                queue.add(JsonObjectRequest)
            }
            layoutManager = LinearLayoutManager(this@CartActivity)
            RecyclerAdapter =
                CartRecyclerAdapter(
                    this@CartActivity,
                    restaurantOrder
                )
            RecyclerView.setAdapter(CartRecyclerAdapter(this@CartActivity, restaurantOrder))
            RecyclerView.adapter = RecyclerAdapter
            RecyclerView.layoutManager = layoutManager
        } else {
            val dailog = AlertDialog.Builder(this@CartActivity)
            dailog.setTitle("No Internet Connection")
            dailog.setMessage("Check your Internet Settings and try again")
            dailog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@CartActivity)
            }
            dailog.create()
            dailog.show()
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Place Order"
    }
}