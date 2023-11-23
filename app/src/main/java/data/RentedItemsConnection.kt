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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class RentedItemsConnection() {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val rentedItems = mutableListOf<RentedItem>()
    private val rentedItemsById = mutableListOf<RentedItem>()
    private val itemList = mutableListOf<Item>()
    val DB = EnvVariableLoader.DB
    val PASSWORD = EnvVariableLoader.PASSWORD
    val PASSWORD_LOCAL = EnvVariableLoader.PASSWORD_LOCAL
    val URL = EnvVariableLoader.URL
    val LOCAL_URL = EnvVariableLoader.URL_LOCAL
    private var successCheckAvailability: Boolean = false


    init {
        modelConfig.serverURL = URL("${URL}/xmlrpc/2/object")
    }

    suspend fun returnRentedItems(): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        try {
            rentedItems.clear()
            val authManager = AuthManager.getInstance()

            val rentalOrderSearch = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, authManager.getUid(), authManager.getPassword(),
                    "sale.order.line", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "order_partner_id", "state", "end_date", "order_id", "product_id"))
                )
            ) as Array<*>

            for (data in rentalOrderSearch) {
                var shouldAddItem = true
                val rentedItem = RentedItem(0, 0, 0, 0, "", "", "", "", "")

                for (item in data as Map<*, *>) {
                    if (item.key == "id") {
                        rentedItem.id = item.value as Int
                    }
                    if (item.key == "name") {
                        rentedItem.name = item.value.toString()
                    }
                    if (item.key == "state") {
                        if (item.value.toString() == "cancel") {
                            shouldAddItem = false
                            break
                        } else {
                            rentedItem.state = item.value.toString()
                        }
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
                if (shouldAddItem) {
                    rentedItems.add(rentedItem)
                }
            }
        } catch (e: Exception) {
            Log.i("odoo", "crashed :( $e")
            throw error(e)
            return@withContext mutableListOf()
        }

        return@withContext rentedItems
    }

    suspend fun returnRentedItemsById(): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        val rentedItems = returnRentedItems()
        rentedItemsById.clear()
        val currentUser = AuthManager.getInstance().getCurrentUser()
        Log.i("odoo", "current user : ${currentUser}")
        Log.i("odoo", "current user's partner id : ${currentUser.partnerId}")
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

    suspend fun createItemRent(startDate: String, endDate: String, productId: Int): Int = withContext(Dispatchers.IO) {
        var orderId = 0
        val currentUser = AuthManager.getInstance().getCurrentUser()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDateAsDate: LocalDate
        val endDateAsDate: LocalDate

        //Check whether inputs startDate and endDate are in date format or not.
        try {
            startDateAsDate = LocalDate.parse(startDate, dateFormatter)
            endDateAsDate = LocalDate.parse(endDate, dateFormatter)
        } catch (e: DateTimeParseException) {
            Log.i("odoo", "Invalid date format for start date or end date.")
            return@withContext -1
        }

        if (endDateAsDate < startDateAsDate) {
            Log.i("odoo", "End date cannot be earlier than start date.")
            return@withContext 0
        }

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

        Log.i("odoo", "Rent successfully confirmed with $orderId")
        return@withContext 1
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
    suspend fun deleteOrder(salesOrderId: Int): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        try {
            // Model for canceling order
            val confirmCancel = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order", "action_cancel",
                    listOf(listOf(salesOrderId)) // id of the order_id to cancel
                )) as Boolean

            if (confirmCancel) {
                println("Rental order successfully canceled")
            } else {
                println("Failed to cancel the rental order.")
            }

            // Model to delete order (you can only delete a canceled order)
            /*    val deleteResult = client.execute(
                    modelConfig,
                    "execute_kw",
                    listOf(
                        DB, 2, PASSWORD,
                        "sale.order", "unlink",
                        listOf(listOf(salesOrderId))
                    )
                ) as Boolean

                // Check if deletion worked
                if (deleteResult) {
                    println("Sales order deleted successfully.")
                } else {
                    println("Error deleting the sales order.")
                } */

        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        }


        return@withContext rentedItems
    }
    suspend fun returnItems(): MutableList<Item> = withContext(Dispatchers.IO) {
        val itemsList : Array<*>;
        val authManager = AuthManager.getInstance()
        try {
            itemsList = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, authManager.getUid(), authManager.getPassword(),
                    "product.product", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "id", "rental", "virtual_available"))
                )
            ) as Array<*>

            if (itemsList.isNotEmpty()) {
                for (item in itemsList) {
                    // Ensure item is a Map (dictionary)
                    if (item is Map<*, *>) {
                        // Make sure the item is rentable
                        if(item["rental"] == true){
                            val name = item["name"]
                            val id = item["id"]
                            val quantity = (item["virtual_available"] as? Double) ?: 0.0
                            if (id is Int) {
                                // Create new item with fetched data and default available value true
                                var newItem = Item(name.toString(), id, "Available", quantity)
                                itemList.add(newItem)
                            }
                        }
                    }
                }
            } else {
                Log.i("odoo", "No items found.")
            }
            return@withContext itemList

        } catch (e: EOFException) {
            // Handle unexpected end of stream exception
            Log.i("odoo", "Error 1 in ItemConnection class: $e")
        } catch (e: Exception) {
            // Handle other exceptions as needed
            Log.i("odoo", "Error 2 in ItemConnection class: $e")
        }

        return@withContext mutableListOf();
    }

    suspend fun returnItemsWithAvailibilityStatus(): MutableList<Item> {
        // if any of those two fails, throw error
        val returnItems = returnItems()
        val returnRentedItems = returnRentedItems()
        val idList = returnRentedItems.map { it.name }
        Log.i("odoo", " Currently rented items : $returnRentedItems")
        // Check availability
        return returnItems
    }
}