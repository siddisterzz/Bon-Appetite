package com.siddhesh.foodrunner.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.adapter.DescriptionRecyclerAdapter
import com.siddhesh.foodrunner.adapter.OrderRecyclerAdapter
import com.siddhesh.foodrunner.modal.ItemDetails
import com.siddhesh.foodrunner.modal.RestaurantOrderHistory
import com.siddhesh.foodrunner.util.ConnectionManager
import org.json.JSONException


class OrderFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: OrderRecyclerAdapter
    lateinit var RecyclerView: RecyclerView
    lateinit var ProgressLayout: RelativeLayout
    lateinit var ProgressBar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences
    val OrderHistory = arrayListOf<RestaurantOrderHistory>()
    val OrderedItem = arrayListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        RecyclerView = view.findViewById(R.id.RecyclerView)
        ProgressLayout = view.findViewById(R.id.ProgressLayout)
        ProgressBar = view.findViewById(R.id.ProgressBar)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/${sharedPreferences.getString(
            "user_id",
            ""
        )}"
        ProgressLayout.visibility = View.VISIBLE
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val JsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        try {
                            ProgressLayout.visibility = View.GONE
                            val Data = data.getJSONArray("data")
                            for (i in 0 until Data.length()) {
                                val foodDetail = arrayListOf<ItemDetails>()
                                val order = Data.getJSONObject(i)
                                val fooditem = order.getJSONArray("food_items")
                                for (j in 0 until fooditem.length()) {
                                    val detail = fooditem.getJSONObject(j)
                                    val itemDetails = ItemDetails(
                                        detail.getString("food_item_id"),
                                        detail.getString("name"),
                                        detail.getString("cost")
                                    )
                                    foodDetail.add(itemDetails)
                                }
                                val orderDetails = RestaurantOrderHistory(
                                    order.getString("order_id"),
                                    order.getString("restaurant_name"),
                                    order.getString("total_cost"),
                                    order.getString("order_placed_at"),
                                    foodDetail
                                )
                                OrderHistory.add(orderDetails)
                            }
                            layoutManager = LinearLayoutManager(activity as Context)
                            RecyclerAdapter =
                                OrderRecyclerAdapter(
                                    activity as Context,
                                    OrderHistory
                                )
                        } catch (e1: JSONException) {
                            Toast.makeText(activity, "Some Error Occurred", Toast.LENGTH_SHORT)
                                .show()
                        }
                        RecyclerView.adapter =
                            OrderRecyclerAdapter(activity as Context, OrderHistory)
                        RecyclerView.adapter = RecyclerAdapter
                        RecyclerView.layoutManager = layoutManager
                    } else {
                        Toast.makeText(activity, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }
                },
                    Response.ErrorListener {
                        if (activity != null) {
                            Toast.makeText(activity, "Some Error Occurred ", Toast.LENGTH_SHORT)
                                .show()
                        }
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
        return view
    }
}