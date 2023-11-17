package com.example.solitawarehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import com.example.solita_warehouse.adapters.OwnRentedItemsAdapter
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoansFragment : Fragment() {
    lateinit var ownLoansButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loans, container, false)
        ownLoansButton = rootView.findViewById(R.id.itemOwnRentedButton)
        val rentedOwnItemsConnection = RentedItemsConnection()

        ownLoansButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val returnRentedItemsById = rentedOwnItemsConnection.returnRentedItemsById()

                val rvRentedItems = rootView.findViewById<View>(R.id.rvOwnRentedItems) as RecyclerView
                val adapter = OwnRentedItemsAdapter(returnRentedItemsById)

                rvRentedItems.adapter = adapter
                rvRentedItems.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return rootView
    }

}