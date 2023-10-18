package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class ItemConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val authService = AuthenticationService(baseUrl)

    init {
        modelConfig.serverURL = URL("$baseUrl/xmlrpc/2/object")
    }

    suspend fun authenticate(): Any? = withContext(Dispatchers.IO) {
        authService.authenticate("db", "admin", "admin")
    }

    suspend fun returnItems(): Any? = withContext(Dispatchers.IO) {
        val auth = authenticate()
        if (auth is Boolean) {
            Log.i("odoo", "returnItems func cannot be ran!")
            return@withContext ""
        }
        val itemsList = client.execute(
            modelConfig,
            "execute_kw",
            listOf(
                db, auth, password,
                "product.product", "read",
                listOf(arrayOf(45,46)),
                mapOf("fields" to listOf("name", "lst_price"))
            )
        ) as Array<*>

        if (itemsList.isNotEmpty()) {
            for (item in itemsList) {
                Log.i("odoo", item.toString())
            }
        } else {
            Log.i("odoo", "No items found.")
        }

        return@withContext itemsList
    }

}