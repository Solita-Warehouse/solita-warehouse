package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Item
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class ItemConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val authService = AuthenticationService(baseUrl)

    private val itemList = mutableListOf<Item>()

    init {
        modelConfig.serverURL = URL("$baseUrl/xmlrpc/2/object")
    }

    suspend fun returnItems(): MutableList<Item> = withContext(Dispatchers.IO) {
        val auth = authService.authenticate("db", "admin", "admin")
        if (auth is Boolean) {
            Log.i("odoo", "returnItems func cannot be ran!")
            return@withContext mutableListOf()
        }
        val itemsList = client.execute(
            modelConfig,
            "execute_kw",
            listOf(
                db, auth, password,
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
    }

}