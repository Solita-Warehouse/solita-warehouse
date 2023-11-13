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
import data.RentedItemsConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.async

class RentFragment : Fragment() {
    private lateinit var itemButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_rent, container, false)
        itemButton = rootView.findViewById(R.id.itemButton)

        itemButton.setOnClickListener {
            //Could be defined in ViewModel
            CoroutineScope(Dispatchers.Main).launch {
                // Get all rentable item form Odoo
                try {
                    val itemConnection =  ItemConnection()
                    //var returnItems = itemConnection.returnItems()

                    // Fetch all rental_orders
                    val rentedItemsConnection = RentedItemsConnection()
                    //val returnRentedItems = rentedItemsConnection.returnRentedItems()

                    // Use async to fetch data concurrently
                    val itemsDeferred = async { itemConnection.returnItems() }
                    val rentedItemsDeferred = async { rentedItemsConnection.returnRentedItems() }

                    // Wait for both deferred values to be available
                    val returnItems = itemsDeferred.await()
                    val returnRentedItems = rentedItemsDeferred.await()

                    // Check if the list is empty,
                    // bad solution because it'll throw an error if there are no rent
                    // Could be better to lift up the try catch here and remove it from functions
                    // in ItemConnection
                    if (returnRentedItems.isEmpty()) {
                        throw IllegalStateException("The list of rented items is empty.")
                    }

                    val idList = returnRentedItems.map { it.name }
                    Log.i("odoo", " Currently rented items : $returnRentedItems")
                    // Check availability
                    for (item in returnItems) {
                        if (idList.contains(item.name)) {
                            item.setAvailableStatus(false)
                        }
                    }
                    // Use recycler view to display fetched item
                    Log.i("odoo", " All rentable items : $returnItems")
                    val rvItems = rootView.findViewById<View>(R.id.rvItems) as RecyclerView
                    val adapter = ItemAdapter(returnItems)

                    rvItems.adapter = adapter
                    rvItems.layoutManager = LinearLayoutManager(requireContext())

                } catch (e: Exception) {
                // Handle exceptions
                Log.e("odoo", "Error fetching data: ${e.message}", e)
            }

            }
        }

        return rootView
    }

}