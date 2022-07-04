package com.siddhesh.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.siddhesh.foodrunner.Activity.CartActivity
import com.siddhesh.foodrunner.R
import com.siddhesh.foodrunner.modal.RestrauntItem

class DescriptionRecyclerAdapter(
    val context: Context,
    val itemList: ArrayList<RestrauntItem>,
    val proceedToCart: Button,
    val ProceedToCart: RelativeLayout,
    val Title: String?
) : RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>() {
    lateinit var proceedtoCart: RelativeLayout
    var s = 0
    var itemSelectedId = arrayListOf<String>()
    var itemSelectedCost = arrayListOf<String>()
    var itemSelectedName = arrayListOf<String>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescriptionViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_description, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(
        holder: DescriptionRecyclerAdapter.DescriptionViewHolder,
        position: Int
    ) {
        val RestaurantItem = itemList[position]
        holder.txtName.text = RestaurantItem.name
        holder.txtNumber.text = (position + 1).toString()
        holder.txtRate.text = RestaurantItem.costForOne
        proceedtoCart = ProceedToCart
        holder.btnAdd.setOnClickListener {
            if (holder.btnAdd.text.toString().equals("Add")) {
                itemSelectedId.add(RestaurantItem.id)
                itemSelectedCost.add(RestaurantItem.costForOne)
                itemSelectedName.add(RestaurantItem.name)
                holder.btnAdd.setBackgroundResource(R.color.Add)
                holder.btnAdd.setText("remove")
                s = s + 1
            } else {
                itemSelectedId.remove(RestaurantItem.id)
                itemSelectedCost.remove(RestaurantItem.costForOne)
                itemSelectedName.remove(RestaurantItem.name)
                holder.btnAdd.setBackgroundResource(R.color.colorPrimary)
                holder.btnAdd.setText("Add")
                s = s - 1
            }
            if (s > 0) {
                proceedtoCart.visibility = View.VISIBLE
            } else {
                proceedtoCart.visibility = View.INVISIBLE
            }
        }
        proceedToCart.setOnClickListener {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra("name", itemSelectedName)
            intent.putExtra("id", itemSelectedId)
            intent.putExtra("restaurantId", RestaurantItem.restrauntId)
            intent.putExtra("costForOne", itemSelectedCost)
            intent.putExtra("title", Title)
            context.startActivity(intent)
        }
    }

    class DescriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNumber: TextView = view.findViewById(R.id.txtNumber)
        val txtName: TextView = view.findViewById(R.id.txtItemName)
        val txtRate: TextView = view.findViewById(R.id.txtrate)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
    }
}