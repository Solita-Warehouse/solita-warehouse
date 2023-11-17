package data

import android.util.Log
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.EnvVariableLoader
import model.Item
import model.RentedItem
import model.User
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.io.EOFException
import java.net.URL


class RentedItemsConnection() {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val rentedItems = mutableListOf<RentedItem>()
    private val rentedItemsById = mutableListOf<RentedItem>()
    val DB = EnvVariableLoader.DB
    val PASSWORD = EnvVariableLoader.PASSWORD
    val PASSWORD_LOCAL = EnvVariableLoader.PASSWORD_LOCAL
    val URL = EnvVariableLoader.URL
    val LOCAL_URL = EnvVariableLoader.URL_LOCAL


    init {
        modelConfig.serverURL = URL("${URL}/xmlrpc/2/object")
    }

    suspend fun returnRentedItems(): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        try {
            rentedItems.clear()
            val rentalOrderSearch = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order.line", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "order_partner_id", "state", "end_date", "order_id", "product_id"))
                )
            ) as Array<*>

            for (data in rentalOrderSearch) {
                val rentedItem = RentedItem(0, 0, 0, 0, "", "", "", "", "")
                for (item in data as Map<*, *>) {
                    if (item.key == "id") {
                        rentedItem.id = item.value as Int
                    }
                    if (item.key == "name") {
                        rentedItem.name = item.value.toString()
                    }
                    if (item.key == "state") {
                        rentedItem.state = item.value.toString()
                    }
                    if (item.key == "end_date") {
                        rentedItem.endDate = item.value.toString()
                    }
                    if (item.key == "order_partner_id") {
                        if (item.value is Array<*>) {
                            for (subItem in item.value as Array<*>) {
                                if (subItem is Int) {
                                    rentedItem.partnerId = subItem
                                } else {
                                    rentedItem.renter = subItem.toString()
                                }
                            }
                        }
                    }
                    if (item.key == "order_id") {
                        if (item.value is Array<*>) {
                            for (subItem in item.value as Array<*>) {
                                if (subItem is Int) {
                                    rentedItem.orderId = subItem
                                } else {
                                    rentedItem.orderName = subItem.toString()
                                }
                            }
                        }
                    }
                    if (item.key == "product_id") {
                        if (item.value is Array<*>) {
                            for (subItem in item.value as Array<*>) {
                                if (subItem is Int) {
                                    rentedItem.productId = subItem
                                }
                            }
                        }
                    }
                }
                rentedItems.add(rentedItem)
            }
        } catch (e: Exception) {
            Log.i("odoo", "crashed :( $e")
            return@withContext mutableListOf();
        }
        return@withContext rentedItems
    }

    suspend fun returnRentedItemsById(): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        val rentedItems = returnRentedItems()
        rentedItemsById.clear()

        for (item in rentedItems) {
            if (item.partnerId == currentUser.partnerId) {
                rentedItemsById.add(item)
            }
        }

        for (item in rentedItemsById) {
            Log.i("odoo", item.toString())
        }
        return@withContext rentedItemsById
    }

    suspend fun createItemRent(startDate: String, endDate: String, productId: Int): String = withContext(Dispatchers.IO) {
        var orderId = 0

        //Creates new rental order.
        try {
            val orderValues = mapOf(
                "partner_id" to currentUser.partnerId,
                "type_id" to 2
            )
            // Create the sales order
            val createResult = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order", "create",
                    listOf(listOf(orderValues))
                )
            ) as Array<*>

            orderId = createResult[0] as Int

            // Confirm the sales order (replace with the correct method name)
            val confirmResult = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order", "action_confirm",  // Replace with the correct method name
                    listOf(listOf(orderId))
                )
            ) as Boolean  // Assuming it returns a boolean indicating success

            if (confirmResult) {
                Log.i("odoo", "Sales order successfully confirmed with ID: $orderId")
            } else {
                Log.i("odoo", "Failed to confirm the sales order.")
            }
        } catch (e: Exception) {
            Log.i("odoo", "Error creating or confirming the sales order: ${e.message}")
        }

        //This adds the rented item to the rental order with hardcoded values at the moment.
        try {
            val orderLineValues = mapOf(
                "order_id" to orderId,
                "product_id" to productId,
                "start_date" to startDate,
                "end_date" to endDate
            )

            val orderLine = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order.line", "create",
                    listOf(orderLineValues)
                )) as Int
        } catch (e: Exception) {
            Log.i("odoo", "Something went wrong adding item to rental order.")
        }

        return@withContext ""
    }

    //Just useful when you want to find out all the item id's, otherwise useless, delete later.
    suspend fun getItemIds(): String = withContext(Dispatchers.IO) {
        val rentalOrderSearch = client.execute(
            modelConfig,
            "execute_kw",
            listOf(
                DB, 2, PASSWORD,
                "product.product", "search_read",
                listOf(emptyList<Any>()),
                mapOf("fields" to listOf("id", "name"))
            )
        ) as Array<*>

        for (item in rentalOrderSearch) {
            Log.i("odoo", item.toString())
        }
        return@withContext ""
    }
}