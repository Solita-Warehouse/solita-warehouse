package com.example.solita_warehouse.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.ModelManager
import com.example.solita_warehouse.R
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.RentedItem

class OwnRentedItemsAdapter(private val mOwnRentedItems: List<RentedItem>) : RecyclerView.Adapter<OwnRentedItemsAdapter.ViewHolder>() {

    inner class ViewHolder(ownRentedItemsView: View) : RecyclerView.ViewHolder(ownRentedItemsView) {
        val nameTextOwnRentedItem = ownRentedItemsView.findViewById<TextView>(R.id.rentedOwnItemNameTitle)
        val endDateTextReturnDate = ownRentedItemsView.findViewById<TextView>(R.id.rentedOwnItemEndDate)
        val buttonItemAction = ownRentedItemsView.findViewById<Button>(R.id.buttonItemAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val rentedOwnItemsView = inflater.inflate(R.layout.item_list_your_own_rents, parent, false)

        return ViewHolder(rentedOwnItemsView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rentedItem: RentedItem = mOwnRentedItems[position]
        val rentedOwnNameTextView = viewHolder.nameTextOwnRentedItem
        val endDateNameTextView = viewHolder.endDateTextReturnDate
        val buttonItemAction = viewHolder.buttonItemAction

        rentedOwnNameTextView.text = rentedItem.name
        endDateNameTextView.text = rentedItem.endDate

        // Handle button click for each item
        buttonItemAction.setOnClickListener {
            Log.i("odoo", "Returning item, id: ${rentedItem.productId} - name: ${rentedItem.name}")
            ModelManager.setItem(rentedItem.name)
            it.findNavController().navigate(R.id.action_to_returnItem)
        }
    }

    override fun getItemCount(): Int {
        return mOwnRentedItems.size
    }
}

