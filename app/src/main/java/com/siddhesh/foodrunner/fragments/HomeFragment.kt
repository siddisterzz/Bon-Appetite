package com.siddhesh.foodrunner.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import androidx.appcompat.widget.SearchView
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.adapter.HomeRecyclerAdapter
import com.siddhesh.foodrunner.modal.Restaurant
import com.siddhesh.foodrunner.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class HomeFragment : Fragment() {
    val RestrauntInfoList = arrayListOf<Restaurant>()
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: HomeRecyclerAdapter
    lateinit var ProgressLayout: RelativeLayout
    lateinit var ProgressBar: ProgressBar
    var ratingComparator = Comparator<Restaurant> { restaurant1, restaurant2 ->
        if (restaurant1.rating.compareTo(restaurant2.rating, true) != 0) {
            restaurant1.rating.compareTo(restaurant2.rating, true)
        } else {
            restaurant1.name.compareTo(restaurant2.name, true)
        }

    }
    var costComparator = Comparator<Restaurant> { restaurant1, restaurant2 ->
        if (restaurant1.costForOne.compareTo(restaurant2.costForOne, true) != 0) {
            restaurant1.costForOne.compareTo(restaurant2.costForOne, true)
        } else {
            restaurant1.name.compareTo(restaurant2.name, true)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.RecyclerView)
        ProgressLayout = view.findViewById(R.id.ProgressLayout)
        ProgressBar = view.findViewById(R.id.ProgressBar)
        setHasOptionsMenu(true)
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        ProgressLayout.visibility = View.VISIBLE
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    ProgressLayout.visibility = View.GONE
                    if (success) {
                        try {
                            val Data = data.getJSONArray("data")
                            for (i in 0 until Data.length()) {
                                val jsonObject = Data.getJSONObject(i)
                                val Restraunt = Restaurant(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("cost_for_one"),
                                    jsonObject.getString("image_url")
                                )
                                RestrauntInfoList.add(Restraunt)
                            }

                            layoutManager = LinearLayoutManager(activity)
                            RecyclerAdapter =
                                HomeRecyclerAdapter(
                                    activity as Context,
                                    RestrauntInfoList
                                )
                            recyclerView.adapter = RecyclerAdapter
                            recyclerView.layoutManager = layoutManager

                        } catch (e1: JSONException) {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Some Error Occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                    ,
                    Response.ErrorListener {
                        if (activity != null) {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
            val dailog = AlertDialog.Builder(activity as Context)
            dailog.setTitle("Connection Error")
            dailog.setMessage("Check your Internet Connection")
            dailog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dailog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dailog.create()
            dailog.show()
        }

        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.sort_rating) {
            Collections.sort(RestrauntInfoList, ratingComparator)
            RestrauntInfoList.reverse()
        } else if (id == R.id.sort_cost_high_to_low) {
            Collections.sort(RestrauntInfoList, costComparator)
            RestrauntInfoList.reverse()
        } else {
            Collections.sort(RestrauntInfoList, costComparator)
        }
        RecyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}