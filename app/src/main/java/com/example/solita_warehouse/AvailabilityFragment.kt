package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.solita_warehouse.R
import data.LoginConnection
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.EnvVariableLoader

class AvailabilityFragment : Fragment() {
    private lateinit var mainTitle : TextView
    private lateinit var orderPartner : EditText
    private lateinit var orderType : EditText
    private lateinit var rentButton: Button
    private lateinit var loginNameText : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_availability, container, false)
        mainTitle = rootView.findViewById(R.id.mainTitle4)
        orderPartner = rootView.findViewById(R.id.orderPartner)
        orderType = rootView.findViewById(R.id.orderType)
        rentButton = rootView.findViewById(R.id.rentButton)
        loginNameText = rootView.findViewById(R.id.loginNameText4)
        var inputOrderPartner = ""
        var inputOrderType = ""
        val URL = EnvVariableLoader.URL
        val DB = EnvVariableLoader.DB
        val URL_LOCAL = EnvVariableLoader.URL_LOCAL
        val rentedItemsConnection = RentedItemsConnection()

        orderPartner.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputOrderPartner = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })


        orderType.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputOrderType = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        rentButton.setOnClickListener {
            Log.d("odoo", "### AUTHENTICATION ###");
            CoroutineScope(Dispatchers.Main).launch {
                val returnOrder = rentedItemsConnection.rentItem()
            }
        }

        return rootView
    }

}