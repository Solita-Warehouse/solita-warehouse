package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.solita_warehouse.R
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.RentedItem

class ReturnDialogFragment : DialogFragment() {
    private lateinit var cancelButton1: Button
    private lateinit var confirmButton1 : Button
    private lateinit var rentedItem : RentedItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_return_dialog, container, false)
        cancelButton1 = rootView.findViewById(R.id.cancelButton1)
        confirmButton1 = rootView.findViewById(R.id.confirmButton1)
        val rentedItemsConnection = RentedItemsConnection()

        cancelButton1.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                dismiss()
            }
        }

        confirmButton1.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                Log.i("odoo", "Returning item, id: ${rentedItem.productId} - name: ${rentedItem.name} - orderId: ${rentedItem.orderId}")
                val deleteOrder = rentedItemsConnection.deleteOrder(rentedItem.orderId)
                dismiss()
        }
    }
        return rootView
    }

    fun setItemData(item: RentedItem) {
        rentedItem = item
    }

}