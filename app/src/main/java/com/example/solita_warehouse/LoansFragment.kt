package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.solita_warehouse.R
import com.example.solita_warehouse.adapters.ItemAdapter
import model.Item

class LoansFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loans, container, false)

        val itemList = ArrayList<Item>()
        val item1 = Item("Hammer", "18.09.2023")
        val item2 = Item("Saw", "12.10.2023")

        itemList.add(item1)
        itemList.add(item2)

        Log.i("Lista", itemList.toString())

        val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
        val adapter = ItemAdapter(itemList)

        rvItems.adapter = adapter

        rvItems.layoutManager = LinearLayoutManager(requireContext())


        return rootView
    }

}