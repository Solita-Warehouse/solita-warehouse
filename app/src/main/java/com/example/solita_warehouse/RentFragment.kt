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
import com.example.solita_warehouse.adapters.ItemAdapter
import data.ItemConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentFragment : Fragment() {
    private lateinit var itemButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_rent, container, false)
        itemButton = rootView.findViewById(R.id.itemButton)

        itemButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val itemConnection =  ItemConnection("http://10.0.2.2:8069", "db", "admin", "admin")
                val returnItems = itemConnection.returnItems()

                Log.i("odoo", returnItems.toString())
                val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
                val adapter = ItemAdapter(returnItems)

                rvItems.adapter = adapter
                rvItems.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        return rootView
    }

}