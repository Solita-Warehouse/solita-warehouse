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
            Item("Nail Gun", "26.07.2024"),
            Item("Ratchet Set", "07.08.2024"),
            Item("Circular Saw", "19.09.2024"),
            Item("Soldering Iron", "01.10.2024"),
            Item("Angle Grinder", "12.11.2024"),
            Item("Hacksaw", "23.12.2024"),
            Item("Caulking Gun", "04.01.2025"),
            Item("Wire Stripper", "15.02.2025"),
            Item("Utility Knife", "28.03.2025"),
            Item("Mallet", "09.04.2025"),
            Item("Screw Gun", "20.05.2025"),
            Item("Ladder", "01.06.2025"),
            Item("Cordless Drill", "12.07.2025"),
            Item("Socket Set", "23.08.2025"),
            Item("Chisel", "04.09.2025"),
            Item("Clamp", "15.10.2025"),
            Item("Wire Cutter", "26.11.2025"),
            Item("Carpenter's Square", "07.12.2025"),
            Item("Hedge Trimmer", "18.01.2026"),
            Item("Toolbox", "29.02.2026"),
            Item("Power Washer", "12.03.2026"),
            Item("Ratchet Set", "07.08.2024"),
            Item("Circular Saw", "19.09.2024"),
            Item("Soldering Iron", "01.10.2024"),
            Item("Angle Grinder", "12.11.2024"),
            Item("Hacksaw", "23.12.2024"),
            Item("Caulking Gun", "04.01.2025"),
            Item("Wire Stripper", "15.02.2025"),
            Item("Utility Knife", "28.03.2025"),
            Item("Mallet", "09.04.2025"),
            Item("Screw Gun", "20.05.2025"),
            Item("Ladder", "01.06.2025"),
            Item("Cordless Drill", "12.07.2025"),
            Item("Socket Set", "23.08.2025"),
            Item("Chisel", "04.09.2025"),
            Item("Clamp", "15.10.2025"),
            Item("Wire Cutter", "26.11.2025"),
            Item("Carpenter's Square", "07.12.2025"),
            Item("Hedge Trimmer", "18.01.2026"),
            Item("Toolbox", "29.02.2026"),
            Item("Power Washer", "12.03.2026")
        )

        val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
        val adapter = ItemAdapter(itemList)

        rvItems.adapter = adapter
        rvItems.layoutManager = LinearLayoutManager(requireContext())

        return rootView
    }

}