package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.example.solita_warehouse.R
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.EnvVariableLoader

class AvailabilityFragment : Fragment() {
    private lateinit var mainTitle : TextView
    private lateinit var startDate : EditText
    private lateinit var endDate : EditText
    private lateinit var rentButton: Button
    private lateinit var loginNameText : TextView
    private lateinit var productIdMenu : Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_availability, container, false)
        mainTitle = rootView.findViewById(R.id.mainTitle4)
        startDate = rootView.findViewById(R.id.startDate)
        endDate = rootView.findViewById(R.id.endDate)
        rentButton = rootView.findViewById(R.id.rentButton)
        loginNameText = rootView.findViewById(R.id.loginNameText4)
        productIdMenu = rootView.findViewById(R.id.productIdSpinner)
        val productOptions = arrayOf("Binoculars", "Black Pen", "Blue Silver Pen", "Measure Tape", "Red Pen",
            "Retractable HP Mouse", "Scissors", "Screwdriver Bits Box", "White Tube", "Wrench")

        //Sets up adapter to list the productOptions on a dropdown menu
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productIdMenu.adapter = adapter

        var inputStartDate = ""
        var inputEndDate = ""
        var inputProductId = 0

        val URL = EnvVariableLoader.URL
        val DB = EnvVariableLoader.DB
        val URL_LOCAL = EnvVariableLoader.URL_LOCAL
        val rentedItemsConnection = RentedItemsConnection()

        startDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputStartDate = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        endDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputEndDate = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        //Not very good hardcoded solution. FIX PLS!
        productIdMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedItem = parentView?.getItemAtPosition(position).toString()

                if (selectedItem == "Binoculars") {
                    inputProductId = 14
                }
                if (selectedItem == "Black Pen") {
                    inputProductId = 7
                }
                if (selectedItem == "Blue Silver Pen") {
                    inputProductId = 8
                }
                if (selectedItem == "Measure Tape") {
                    inputProductId = 12
                }
                if (selectedItem == "Red Pen") {
                    inputProductId = 6
                }
                if (selectedItem == "Retractable HP Mouse") {
                    inputProductId = 13
                }
                if (selectedItem == "Scissors") {
                    inputProductId = 9
                }
                if (selectedItem == "Screwdriver Bits Box") {
                    inputProductId = 11
                }
                if (selectedItem == "White Tube") {
                    inputProductId = 10
                }
                if (selectedItem == "Wrench") {
                    inputProductId = 5
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Log.d("SelectedProduct", "Nothing selected")
            }
        }

        rentButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val returnOrder = rentedItemsConnection.createItemRent(inputStartDate, inputEndDate, inputProductId)
            }
        }

        return rootView
    }

}