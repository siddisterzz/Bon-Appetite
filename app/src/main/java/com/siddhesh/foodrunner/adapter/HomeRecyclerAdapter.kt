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
import com.siddhesh.foodrunner.modal.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int
    ) {
        val Restrauntitem = itemList[position]
        holder.txtName.text = Restrauntitem.name
        holder.txtRate.text = Restrauntitem.costForOne
        holder.txtRating.text = Restrauntitem.rating
        Picasso.get().load(Restrauntitem.Image).fit().error(R.drawable.burger_image)
            .into(holder.imgImage)
        holder.llcontent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("title", Restrauntitem.name)
            intent.putExtra("id", Restrauntitem.id)
            context.startActivity(intent)
        }
        val restaurantEntity = RestaurantEntity(
            Restrauntitem.id,
            Restrauntitem.name,
            Restrauntitem.rating,
            Restrauntitem.costForOne,
            Restrauntitem.Image
        )
        //executes the functionality of the DBAsyncTask class
        val checkFav = DBAsyncTask(context, restaurantEntity, 1).execute()
        //gets the value from the doInbackground method
        val isFav = checkFav.get()
        if (isFav) {
            holder.imgFavorites.setImageResource(R.drawable.ic_favourites2)
        } else {
            holder.imgFavorites.setImageResource(R.drawable.ic_favourites)
        }
        holder.imgFavorites.setOnClickListener {
            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.imgFavorites.setImageResource(R.drawable.ic_favourites2)
                    Toast.makeText(
                        context,
                        "${Restrauntitem.name} added to favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            } else {
                val async = DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if (result) {
                    holder.imgFavorites.setImageResource(R.drawable.ic_favourites)
                    Toast.makeText(
                        context,
                        "${Restrauntitem.name} removed favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
            when (Mode) {
                1 -> {
                    val restraunt: RestaurantEntity? =
                        db.restaurantDao().getRestaurantsById(restaurantEntity.restaurant_id)
                    db.close()
                    return restraunt != null
                }
                2 -> {
                    db.restaurantDao().InsertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    db.restaurantDao().DeleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false
        }
    }
}