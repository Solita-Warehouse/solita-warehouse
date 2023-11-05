package data

import android.util.Log
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Item
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.io.EOFException
import java.net.URL

class ItemConnection() {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val itemList = mutableListOf<Item>()
    val dotenv = dotenv {
        directory = "/assets"
        filename = "env" // instead of 'env', use 'env'
    }
    // Access environment variables
    val URL = dotenv["URL"]
    val DB = dotenv["DB"]
    val PASSWORD = dotenv["PASSWORD"]
    val USERNAME = dotenv["USERNAME"]


    init {
        modelConfig.serverURL = URL("$URL/xmlrpc/2/object")
    }

    suspend fun returnItems(): MutableList<Item> = withContext(Dispatchers.IO) {
        var itemsList : Array<*>;
        try {
             itemsList = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "product.product", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "lst_price"))
                )
            ) as Array<*>

            if (itemsList.isNotEmpty()) {
                for (item in itemsList) {
                    // Ensure item is a Map (dictionary)
                    if (item is Map<*, *>) {
                        val name = item["name"]
                        val lstPrice = item["lst_price"]
                        val newItem = Item(name.toString(), lstPrice.toString())
                        itemList.add(newItem)
                    }
                }
            } else {
                Log.i("odoo", "No items found.")
            }

            return@withContext itemList

        } catch (e: EOFException) {
            // Handle unexpected end of stream exception
            e.printStackTrace()
        } catch (e: Exception) {
            // Handle other exceptions as needed
            e.printStackTrace()
        }

        return@withContext mutableListOf();
    }

}