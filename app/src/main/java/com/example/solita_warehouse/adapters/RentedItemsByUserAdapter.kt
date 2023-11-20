package com.example.solita_warehouse.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import model.RentedItem
import org.w3c.dom.Text

class RentedItemsByUserAdapter(private val mRentedItems: List<RentedItem>) : RecyclerView.Adapter<RentedItemsByUserAdapter.ViewHolder>()  {
    inner class ViewHolder(rentedItemView: View) : RecyclerView.ViewHolder(rentedItemView) {
        val nameTextRentedItem = rentedItemView.findViewById<TextView>(R.id.rentedItemNameTitle)
        val nameTextRenter = rentedItemView.findViewById<TextView>(R.id.rentedItemRenter)
        val nameTextEndDate = rentedItemView.findViewById<TextView>(R.id.rentedItemEndDate)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val rentedItemview = inflater.inflate(R.layout.item_list_rent_by_user, parent, false)

        return ViewHolder(rentedItemview)
    }

    override fun onBindViewHolder(viewHolder: RentedItemsByUserAdapter.ViewHolder, position: Int) {
        val rentedItem: RentedItem = mRentedItems.get(position)
        val rentedNameTextView = viewHolder.nameTextRentedItem
        val renterNameTextView = viewHolder.nameTextRenter
        val endDateTextView = viewHolder.nameTextEndDate

        rentedNameTextView.setText(rentedItem.name)
        renterNameTextView.setText(rentedItem.renter)
        endDateTextView.setText(rentedItem.endDate)
    }

    override fun getItemCount(): Int {
        return mRentedItems.size
    }
}