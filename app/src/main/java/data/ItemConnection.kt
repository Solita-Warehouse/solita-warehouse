package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Item
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.io.EOFException
import java.net.URL
import model.EnvVariableLoader

class ItemConnection() {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val itemList = mutableListOf<Item>()
    val DB = EnvVariableLoader.DB
    val PASSWORD = EnvVariableLoader.PASSWORD
    val LOCAL_PASSWORD = EnvVariableLoader.PASSWORD_LOCAL
    val URL = EnvVariableLoader.URL

    init {
        modelConfig.serverURL = URL("${URL}/xmlrpc/2/object")
    }

    suspend fun returnItems(): MutableList<Item> = withContext(Dispatchers.IO) {
        val itemsList : Array<*>;
        try {
             itemsList = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "product.product", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "id"))
                )
            ) as Array<*>

            if (itemsList.isNotEmpty()) {
                for (item in itemsList) {
                    // Ensure item is a Map (dictionary)
                    if (item is Map<*, *>) {
                        val name = item["name"]
                        val id = item["id"]
                        if (id is Int) {
                            val newItem = Item(name.toString(), id)
                            itemList.add(newItem)
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

}