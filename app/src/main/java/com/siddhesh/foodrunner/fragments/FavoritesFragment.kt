package com.siddhesh.foodrunner.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.siddhesh.foodrunner.Database.RestaurantDatabase
import com.siddhesh.foodrunner.Database.RestaurantEntity
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.adapter.FavoriteRecyclerAdapter
import com.siddhesh.foodrunner.adapter.HomeRecyclerAdapter

class FavoritesFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var RecyclerAdapter: FavoriteRecyclerAdapter
    lateinit var ProgressLayout: RelativeLayout
    lateinit var ProgressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = view.findViewById(R.id.RecyclerView)
        ProgressLayout = view.findViewById(R.id.ProgressLayout)
        ProgressBar = view.findViewById(R.id.ProgressBar)
        layoutManager = LinearLayoutManager(activity as Context)
        val dbRestaurantList = RetrieveFavorites(activity as Context).execute().get()
        println("this is $dbRestaurantList")
        if (dbRestaurantList != null && activity != null) {
            ProgressLayout.visibility = View.GONE
            RecyclerAdapter = FavoriteRecyclerAdapter(activity as Context, dbRestaurantList)
            recyclerView.adapter = RecyclerAdapter
            recyclerView.layoutManager = layoutManager
        }
        return view
    }

    class RetrieveFavorites(val context: Context) :
        AsyncTask<Void, Void, List<RestaurantEntity>>() {
        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db")
                .build()
            return db.restaurantDao().getAllRestaurants()
        }

    }
}