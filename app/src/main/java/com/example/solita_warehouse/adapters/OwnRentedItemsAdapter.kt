package com.example.solita_warehouse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import model.RentedItem

class OwnRentedItemsAdapter(private val mOwnRentedItems: List<RentedItem>) : RecyclerView.Adapter<OwnRentedItemsAdapter.ViewHolder>()  {
    inner class ViewHolder(ownRentedItemsView: View) : RecyclerView.ViewHolder(ownRentedItemsView) {
        val nameTextOwnRentedItem = ownRentedItemsView.findViewById<TextView>(R.id.rentedOwnItemNameTitle)
        val nameTextReturnDate = ownRentedItemsView.findViewById<TextView>(R.id.rentedOwnItemEndDate)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val rentedOwnItemsView = inflater.inflate(R.layout.rented_own_items_list, parent, false)

        return ViewHolder(rentedOwnItemsView)
    }

    override fun onBindViewHolder(viewHolder: OwnRentedItemsAdapter.ViewHolder, position: Int) {
        val rentedItem: RentedItem = mOwnRentedItems.get(position)
        val rentedOwnNameTextView = viewHolder.nameTextOwnRentedItem
        val endDateNameTextView = viewHolder.nameTextReturnDate

        rentedOwnNameTextView.setText(rentedItem.name)
        endDateNameTextView.setText(rentedItem.endDate)

    }

    override fun getItemCount(): Int {
        return mOwnRentedItems.size
    }
}
