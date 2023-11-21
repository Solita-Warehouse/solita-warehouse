package com.example.solita_warehouse.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.example.solita_warehouse.R
import com.example.solitawarehouse.RentDialogFragment
import model.Item


class RentItemsAdapter(private val mItems: List<Item>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<RentItemsAdapter.ViewHolder>() {
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
        val itemView = inflater.inflate(R.layout.item_list_rent, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: RentItemsAdapter.ViewHolder, position: Int) {
        val item: Item = mItems.get(position)
        val dialog = RentDialogFragment()

        // Item names
        val nameTextView = viewHolder.nameTextItem
        nameTextView.setText(item.name)

        // Item availability
        val availableTextView = viewHolder.availableTextItem
        availableTextView.setText(item.availability)

        val buttonRent = viewHolder.buttonRentItem

        if (item.availability == "Available") {
            buttonRent.setOnClickListener {
                dialog.setItemData(item)
                dialog.show(fragmentManager, "Rent item dialog")
            }
        } else {
            buttonRent.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

}