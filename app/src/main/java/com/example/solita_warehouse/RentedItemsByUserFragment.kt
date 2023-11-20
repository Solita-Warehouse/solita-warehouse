package com.example.solita_warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.adapters.RentedItemsByUserAdapter
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentedItemsByUserFragment : Fragment() {
    private lateinit var itemButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_renteditems_by_user, container, false)
        itemButton = rootView.findViewById(R.id.itemRentedButton)
        val rentedItemsConnection = RentedItemsConnection()

        itemButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val returnRentedItems = rentedItemsConnection.returnRentedItems()
                val rvRentedItems = rootView.findViewById<View>(R.id.rvRentedItems) as RecyclerView
                val adapter = RentedItemsByUserAdapter(returnRentedItems)

                rvRentedItems.adapter = adapter
                rvRentedItems.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return rootView
    }

}