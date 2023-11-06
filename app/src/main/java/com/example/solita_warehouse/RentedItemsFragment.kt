package com.example.solita_warehouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.adapters.ItemAdapter
import data.ItemConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentedItemsFragment : Fragment() {
    private lateinit var itemButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_renteditems, container, false)
        itemButton = rootView.findViewById(R.id.itemRentedButton)

        itemButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                //val rvItems = rootView.findViewById<View>(R.id.rvRentedItems) as RecyclerView
                //val adapter = ItemAdapter(returnItems)
                //rvItems.adapter = adapter
                //rvItems.layoutManager = LinearLayoutManager(requireContext())
                Log.i("odoo", "rentedItemsFragmentButton pressed")
            }
        }

        return rootView
    }

}