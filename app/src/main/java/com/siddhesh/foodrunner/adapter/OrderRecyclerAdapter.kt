package com.siddhesh.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.modal.RestaurantOrder
import com.siddhesh.foodrunner.modal.RestaurantOrderHistory
import kotlinx.android.synthetic.main.activity_cart.view.*

class OrderRecyclerAdapter(
    val context: Context,
    val OrderDetails: ArrayList<RestaurantOrderHistory>
) : RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder>() {
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val RecyclerView: RecyclerView = view.findViewById(R.id.RecyclerView)
        lateinit var layoutManager: RecyclerView.LayoutManager
        val OrderAt: TextView = view.findViewById(R.id.OrderAt)
        val OrderOn: TextView = view.findViewById(R.id.OrderOn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
        return OrderRecyclerAdapter.OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return OrderDetails.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val itemDetails = OrderDetails[position]
        val item = OrderDetails[position].foodItems
        val RestaurantOrder = arrayListOf<RestaurantOrder>()
        holder.OrderAt.text = itemDetails.restaurantName
        holder.OrderOn.text = itemDetails.OrderPlacedAt.replace('-', '/')
        for (i in 0 until item.size) {
            var itemname = RestaurantOrder(item[i].name, item[i].cost)
            RestaurantOrder.add(itemname)
        }
        val CartRecyclerAdapter: CartRecyclerAdapter
        CartRecyclerAdapter = CartRecyclerAdapter(context, RestaurantOrder)
        holder.layoutManager = LinearLayoutManager(context)
        holder.RecyclerView.layoutManager = holder.layoutManager
        holder.RecyclerView.adapter = CartRecyclerAdapter
    }
}
