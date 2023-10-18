package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import com.example.solita_warehouse.adapters.ItemAdapter
import data.ItemConnection
import data.LoginConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Item

class LoansFragment : Fragment() {
    private lateinit var itemButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loans, container, false)
        itemButton = rootView.findViewById(R.id.itemButton)


        val itemList = listOf(
            Item("Hammer", "18.09.2023"),
            Item("Saw", "12.10.2023"),
            Item("Screwdriver", "25.11.2023"),
            Item("Wrench", "05.12.2023"),
            Item("Drill", "15.01.2024"),
            Item("Pliers", "28.02.2024"),
            Item("Paintbrush", "10.03.2024"),
            Item("Tape Measure", "22.04.2024"),
            Item("Level", "03.05.2024"),
            Item("Chainsaw", "14.06.2024"),

        )

        itemButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val itemConnection =  ItemConnection("http://10.0.2.2:8069", "db", "admin", "admin")
                val returnItems = itemConnection.returnItems()

            }
        }

        val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
        val adapter = ItemAdapter(itemList)

        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(requireContext())

        return rootView
    }

}