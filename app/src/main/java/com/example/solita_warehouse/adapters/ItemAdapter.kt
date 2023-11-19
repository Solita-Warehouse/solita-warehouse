package com.example.solita_warehouse.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import androidx.navigation.findNavController
import com.example.solita_warehouse.ModelManager
import com.example.solita_warehouse.R
import model.Item


class ItemAdapter(private val mItems: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val nameTextItem = itemView.findViewById<TextView>(R.id.itemNameTitle)
        val availableTextItem = itemView.findViewById<TextView>(R.id.itemAvailableTitle)
        val buttonRentItem = itemView.findViewById<Button>(R.id.buttonRentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(R.layout.item_list, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ItemAdapter.ViewHolder, position: Int) {
        val item: Item = mItems.get(position)

        // Item names
        val nameTextView = viewHolder.nameTextItem
        nameTextView.setText(item.name)

        // Item availability
        val availableTextView = viewHolder.availableTextItem
        availableTextView.setText(item.availability)

        val buttonRent = viewHolder.buttonRentItem

        buttonRent.setOnClickListener {
            Log.i("odoo", "Rent item: ${item.id}")
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

}