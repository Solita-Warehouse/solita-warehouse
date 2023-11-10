package com.example.solita_warehouse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import model.Item
import model.RentedItem

class RentedItemAdapter(private val mRentedItems: List<RentedItem>) : RecyclerView.Adapter<RentedItemAdapter.ViewHolder>()  {
    inner class ViewHolder(rentedItemView: View) : RecyclerView.ViewHolder(rentedItemView) {
        val nameTextRentedItem = rentedItemView.findViewById<TextView>(R.id.rentedItemNameTitle)
        val nameTextRenter = rentedItemView.findViewById<TextView>(R.id.rentedItemRenter)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val rentedItemview = inflater.inflate(R.layout.rented_item_list, parent, false)

        return ViewHolder(rentedItemview)
    }

    override fun onBindViewHolder(viewHolder: RentedItemAdapter.ViewHolder, position: Int) {
        val rentedItem: RentedItem = mRentedItems.get(position)
        val rentedNameTextView = viewHolder.nameTextRentedItem
        val renterNameTextView = viewHolder.nameTextRenter

        rentedNameTextView.setText(rentedItem.name)
        renterNameTextView.setText(rentedItem.renter)

    }

    override fun getItemCount(): Int {
        return mRentedItems.size
    }
}