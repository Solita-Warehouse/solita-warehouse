package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import com.example.solita_warehouse.adapters.RentItemsAdapter
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

class RentFragment : Fragment() {
    private lateinit var itemButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_rent, container, false)
        itemButton = rootView.findViewById(R.id.itemButton)

        itemButton.setOnClickListener {
            //Could be defined in ViewModel
            CoroutineScope(Dispatchers.Main).launch {
                // Get all rentable items from Odoo
                try {
                    val rentedItemsConnection = RentedItemsConnection()
                    val returnItems = rentedItemsConnection.returnItems()
                    // Use recycler view to display fetched item
                    Log.i("odoo", " All rentable items : $returnItems")
                    val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
                    val adapter = RentItemsAdapter(returnItems, requireFragmentManager()) //Deprecated, fix later.

                    rvItems.adapter = adapter
                    rvItems.layoutManager = LinearLayoutManager(requireContext())

                } catch (e: Exception) {
                    // Handle exceptions
                    Log.e("odoo", "Error fetching data: ${e.message}", e)
                }

            }
        }

        return rootView
    }

}