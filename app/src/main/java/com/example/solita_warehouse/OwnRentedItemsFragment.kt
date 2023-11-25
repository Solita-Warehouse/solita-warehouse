package com.example.solitawarehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import com.example.solita_warehouse.adapters.OwnRentedItemsAdapter
import data.AuthManager
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class OwnRentedItemsFragment : Fragment() {
    lateinit var ownLoansButton : Button
    lateinit var loggedInAs : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_your_rented_items, container, false)
        ownLoansButton = rootView.findViewById(R.id.itemOwnRentedButton)
        loggedInAs = rootView.findViewById(R.id.loggedInAs)
        val rentedOwnItemsConnection = RentedItemsConnection()
        loggedInAs.text = "You are logged in as: ${AuthManager.getInstance().getCurrentUser().userName}"

        ownLoansButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val returnRentedItemsById = rentedOwnItemsConnection.returnRentedItemsById()
                val rvRentedItems = rootView.findViewById<View>(R.id.rvOwnRentedItems) as RecyclerView
                val adapter = OwnRentedItemsAdapter(returnRentedItemsById, requireFragmentManager())
                rvRentedItems.adapter = adapter
                rvRentedItems.layoutManager = LinearLayoutManager(requireContext())
            }
        }
        return rootView
    }

}