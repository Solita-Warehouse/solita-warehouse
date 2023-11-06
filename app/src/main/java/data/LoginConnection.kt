package data

import android.util.Log
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val authService = AuthenticationService(baseUrl)

    init {
        modelConfig.serverURL = URL("$baseUrl/xmlrpc/2/object")
    }

    suspend fun returnUserData(): String = withContext(Dispatchers.IO) {
        try {
            val auth = authService.authenticate(db, username, password)
            if (auth is Boolean) {
                Log.i("odoo", "returnUserData func cannot be run!")
                return@withContext ""
            }

            val userInformation = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    db, 2, password,
                    "res.users", "read",
                    listOf(2),
                    mapOf("fields" to listOf("login"))
                )
            ) as Array<*>

            // Extract the username from the user information
            val userName = userInformation[0]
            Log.i("odoo", "Username: $userName")
            return@withContext "$userName"
        } catch (e: Exception) {
            // Handle exceptions as needed
            Log.i("odoo", "Error in LoginConnection class: ${e.printStackTrace()}")
            return@withContext "" // Return an empty string or handle the error case as appropriate
        }
    }


}