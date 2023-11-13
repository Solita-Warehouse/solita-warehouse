package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.solita_warehouse.R
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoansFragment : Fragment() {
    lateinit var loansButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loans, container, false)
        loansButton = rootView.findViewById(R.id.loansButton)

        loansButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val rentedItemsConnection = RentedItemsConnection()
                rentedItemsConnection.returnRentedItemsById()
            }
        }

        return rootView
    }

}