package com.siddhesh.foodrunner.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RestaurantDao {
    @Insert
    fun InsertRestaurant(restaurantEntity: RestaurantEntity)

    @Delete
    fun DeleteRestaurant(restaurantEntity: RestaurantEntity)

    @Query("SELECT*FROM restaurants")
    fun getAllRestaurants(): List<RestaurantEntity>

    @Query("SELECT*FROM restaurants WHERE restaurant_id=:restaurantId")
    fun getRestaurantsById(restaurantId: String): RestaurantEntity
}