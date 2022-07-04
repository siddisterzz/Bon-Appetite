package com.siddhesh.foodrunner.modal

class RestaurantOrderHistory(
    val orderId: String,
    val restaurantName: String,
    val totalCost: String,
    val OrderPlacedAt: String,
    val foodItems: ArrayList<ItemDetails>
)