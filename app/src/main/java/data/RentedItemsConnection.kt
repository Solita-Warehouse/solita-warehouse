package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.EnvVariableLoader
import model.Item
import model.RentedItem
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
            val rentalOrderSearch = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order.line", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "order_partner_id", "state"))
                )
            ) as Array<*>

            for(data in rentalOrderSearch) {
                val rentedItem = RentedItem(0, 0, "", "", "")
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
                }
                rentedItems.add(rentedItem)
            }
        } catch(e: Exception) {
            Log.i("odoo", "crashed :( $e")
            return@withContext mutableListOf();
        }
        return@withContext rentedItems
    }

    suspend fun returnRentedItemsById(): MutableList<RentedItem> = withContext(Dispatchers.IO) {
        val rentedItems = returnRentedItems()

        for (item in rentedItems) {
            if (item.partnerId == 7) {
                rentedItemsById.add(item)
            }
        }

        for (item in rentedItemsById) {
            Log.i("odoo", item.toString())
        }
        Log.i("odoo", rentedItemsById.toString())
        return@withContext rentedItemsById
    }

}