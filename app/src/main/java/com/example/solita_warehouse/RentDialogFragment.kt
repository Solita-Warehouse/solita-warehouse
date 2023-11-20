package com.example.solitawarehouse

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.solita_warehouse.R
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.EnvVariableLoader
import model.Item
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class RentDialogFragment : DialogFragment() {
    private lateinit var rentButton: Button
    private lateinit var startDateButton : Button
    private lateinit var endDateButton : Button
    private lateinit var startDateTextView : TextView
    private lateinit var endDateTextView : TextView
    private lateinit var cancelButton : Button
    private lateinit var itemIdText : TextView
    private lateinit var currentItem : Item

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_rent_dialog, container, false)
        rentButton = rootView.findViewById(R.id.rentButton)
        startDateButton = rootView.findViewById(R.id.startDateButton)
        endDateButton = rootView.findViewById(R.id.endDateButton)
        startDateTextView = rootView.findViewById(R.id.startDateTextView)
        endDateTextView = rootView.findViewById(R.id.endDateTextView)
        cancelButton = rootView.findViewById(R.id.cancelButton)
        itemIdText = rootView.findViewById(R.id.itemIdText)

        val rentedItemsConnection = RentedItemsConnection()

        itemIdText.setText("Currently selected item \n${currentItem.name}")

        rentButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (startDateTextView.text.toString() == "Start Date" || endDateTextView.text.toString() == "End Date") {
                    Log.i("odoo", "Either start date or end date was empty.")
                    showAlert("Error", "You cannot leave start or end date empty!")
                } else {
                    val returnOrder = rentedItemsConnection.createItemRent(startDateTextView.text.toString(), endDateTextView.text.toString(), currentItem.id)
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

        cancelButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Log.i("odoo", currentItem.id.toString())
                dismiss()
            }
        }

        return rootView
    }

    fun setItemData(item: Item) {
        currentItem = item
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
                dismiss()
            }
            .create()
        alertDialog.show()
    }

}