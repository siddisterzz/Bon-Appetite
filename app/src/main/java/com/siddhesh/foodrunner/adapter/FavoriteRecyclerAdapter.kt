package com.siddhesh.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.siddhesh.foodrunner.Activity.DescriptionActivity
import com.siddhesh.foodrunner.Database.RestaurantDatabase
import com.siddhesh.foodrunner.Database.RestaurantEntity
import com.siddhesh.foodrunner.R
import com.squareup.picasso.Picasso

class FavoriteRecyclerAdapter(val context: Context, val restaurantList: List<RestaurantEntity>) :
    RecyclerView.Adapter<FavoriteRecyclerAdapter.FavoriteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteRecyclerAdapter.FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FavoriteRecyclerAdapter.FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    override fun onBindViewHolder(
        holder: FavoriteRecyclerAdapter.FavoriteViewHolder,
        position: Int
    ) {
        val restaurant = restaurantList[position]
        holder.txtName.text = restaurant.name
        holder.txtRate.text = restaurant.costForOne
        holder.txtRating.text = restaurant.rating
        Picasso.get().load(restaurant.Image).fit().error(R.drawable.burger_image)
            .into(holder.imgImage)
        holder.imgFavorites.setImageResource(R.drawable.ic_favourites2)
        holder.llcontent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("title", restaurant.name)
            intent.putExtra("id", restaurant.restaurant_id)
            context.startActivity(intent)
        }
        holder.imgFavorites.setOnClickListener {
            if (FavoriteRecyclerAdapter.DBAsyncTask(context, restaurant, 1).execute().get()) {
                val async = FavoriteRecyclerAdapter.DBAsyncTask(context, restaurant, 3).execute()
                val result = async.get()
                if (result) {
                    holder.imgFavorites.setImageResource(R.drawable.ic_favourites)
                    Toast.makeText(
                        context,
                        "${restaurant.name} removed favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.llcontent.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgImage: ImageView = view.findViewById(R.id.imgImage)
        val txtName: TextView = view.findViewById(R.id.txtItemName)
        val txtRate: TextView = view.findViewById(R.id.txtrate)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val llcontent: LinearLayout = view.findViewById(R.id.llContent)
        val imgFavorites: ImageButton = view.findViewById(R.id.imgFavorites)
    }

    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val Mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            db.restaurantDao().DeleteRestaurant(restaurantEntity)
            db.close()
            return true
        }
    }
}