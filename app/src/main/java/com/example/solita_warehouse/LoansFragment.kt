package com.example.solitawarehouse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.solita_warehouse.R
import model.Item

class LoansFragment : Fragment() {

    lateinit var itemList : ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loans, container, false)
        itemList = rootView.findViewById(R.id.itemList)
        var items = arrayOf(
            Item("Hammer", "18.09.2023"),
            Item("Saw", "12.10.2023"),
            Item("Jackhammer", "02.12.2023"),
            Item("Screwdriver", "24.12.2023"),
            Item("Table saw", "18.09.2023"),
            Item(" Measuring tape", "18.06.2023")

        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)

        itemList.adapter = adapter

        return rootView
    }

}