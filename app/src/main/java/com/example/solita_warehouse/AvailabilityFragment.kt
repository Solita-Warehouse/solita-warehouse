package com.example.solitawarehouse

import android.app.AlertDialog
import android.app.DatePickerDialog
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
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AvailabilityFragment : Fragment() {
    private lateinit var mainTitle : TextView
    private lateinit var rentButton: Button
    private lateinit var productIdMenu : Spinner
    private lateinit var startDateButton : Button
    private lateinit var endDateButton : Button
    private lateinit var startDateTextView : TextView
    private lateinit var endDateTextView : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_availability, container, false)
        mainTitle = rootView.findViewById(R.id.mainTitle4)
        rentButton = rootView.findViewById(R.id.rentButton)
        productIdMenu = rootView.findViewById(R.id.productIdSpinner)
        startDateButton = rootView.findViewById(R.id.startDateButton)
        endDateButton = rootView.findViewById(R.id.endDateButton)
        startDateTextView = rootView.findViewById(R.id.startDateTextView)
        endDateTextView = rootView.findViewById(R.id.endDateTextView)

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
                if (startDateTextView.text.toString() == "Start Date" || endDateTextView.text.toString() == "End Date") {
                    Log.i("odoo", "Either start date or end date was empty.")
                    showAlert("Error", "You cannot leave start or end date empty!")
                } else {
                    val returnOrder = rentedItemsConnection.createItemRent(startDateTextView.text.toString(), endDateTextView.text.toString(), inputProductId)
                    showAlert("Success", returnOrder)
                }
            }
        }

        startDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                openDialog(startDateTextView)
            }
        }

        endDateButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                openDialog(endDateTextView)
            }
        }

        return rootView
    }

    fun openDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = SimpleDateFormat("yyyy-MM-dd").format(Date(selectedYear - 1900, selectedMonth, selectedDay))
                textView.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun showAlert(title: String, message : String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                // Do something when the "OK" button is clicked, if needed
            }
            .create()

        alertDialog.show()
    }

}