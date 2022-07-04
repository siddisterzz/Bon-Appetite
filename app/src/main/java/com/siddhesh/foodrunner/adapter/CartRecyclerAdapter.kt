package com.siddhesh.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.modal.RestaurantOrder

class CartRecyclerAdapter(val context: Context, val itemList: ArrayList<RestaurantOrder>) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {


    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtItemName: TextView = view.findViewById(R.id.txtItemName)
        val txtRate: TextView = view.findViewById(R.id.txtrate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.actvity_bill, parent, false)
        return CartRecyclerAdapter.CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = itemList[position]
        holder.txtItemName.text = item.foodName
        holder.txtRate.text = item.Cost
    }
}