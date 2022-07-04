package com.siddhesh.foodrunner.Activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.adapter.DescriptionRecyclerAdapter
import com.siddhesh.foodrunner.modal.RestrauntItem
import com.siddhesh.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_description.*
import kotlinx.android.synthetic.main.list_item_description.*
import org.json.JSONException

class DescriptionActivity : AppCompatActivity() {
    val RestrauntInfoList = arrayListOf<RestrauntItem>()
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: DescriptionRecyclerAdapter
    lateinit var ProgressLayout: RelativeLayout
    lateinit var toolbar: Toolbar
    lateinit var ProgressBar: ProgressBar
    lateinit var ProceedToCart: RelativeLayout
    lateinit var btnProceedToCart: Button
    var Title: String? = "Restaurant Name"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_description)
        recyclerView = findViewById(R.id.RecyclerView)
        ProgressLayout = findViewById(R.id.ProgressLayout)
        ProgressBar = findViewById(R.id.ProgressBar)
        ProceedToCart = findViewById(R.id.btnLayout)
        btnProceedToCart = findViewById(R.id.btnProceedToCart)
        toolbar = findViewById(R.id.toolbar)
        var id: String? = null
        if (intent != null) {
            Title = intent.getStringExtra("title")
            id = intent.getStringExtra("id")
        } else {
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected Error Occurred",
                Toast.LENGTH_SHORT
            ).show()
        }
        setSupportActionBar(toolbar)
        supportActionBar?.title = Title
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"
        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            ProgressLayout.visibility = View.VISIBLE
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    println("Response is $it")
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    ProgressLayout.visibility = View.GONE
                    if (success) {
                        try {
                            val Data = data.getJSONArray("data")
                            for (i in 0 until Data.length()) {
                                val jsonObject = Data.getJSONObject(i)
                                val item = RestrauntItem(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("cost_for_one"),
                                    jsonObject.getString("restaurant_id")
                                )
                                println("this is $item")
                                RestrauntInfoList.add(item)
                                layoutManager = LinearLayoutManager(this@DescriptionActivity)
                                RecyclerAdapter =
                                    DescriptionRecyclerAdapter(
                                        this@DescriptionActivity,
                                        RestrauntInfoList, btnProceedToCart, ProceedToCart, Title
                                    )
                                recyclerView.setAdapter(
                                    DescriptionRecyclerAdapter(
                                        this@DescriptionActivity,
                                        RestrauntInfoList,
                                        btnProceedToCart,
                                        ProceedToCart,
                                        Title
                                    )
                                )
                                recyclerView.adapter = RecyclerAdapter
                                recyclerView.layoutManager = layoutManager
                            }
                        } catch (e1: JSONException) {

                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                    ,
                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "d9eeb061efbb60"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)


        } else {
            val dailog = AlertDialog.Builder(this@DescriptionActivity)
            dailog.setTitle("No Internet Connection")
            dailog.setMessage("Check your Internet Settings and try again")
            dailog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dailog.create()
            dailog.show()
        }
    }
}
