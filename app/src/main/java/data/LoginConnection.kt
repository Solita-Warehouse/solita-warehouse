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
        val auth = authService.authenticate(db, username, password)
        if (auth is Boolean) {
            Log.i("odoo", "returnUserData func cannot be ran!")
            return@withContext ""
        }
        val userInformation = client.execute(
            modelConfig,
            "execute_kw",
            listOf(
                db, auth, password, // Use the authenticate method to get the user ID
                "res.users", "read",
                listOf(auth), // Pass the user's uid to read their information
                mapOf("fields" to listOf("login")) // Specify the fields you want to retrieve, in this case, the "login" field
            )
        ) as Array<*>

        // Extract the username from the user information
        val userName = userInformation[0]
        Log.i("odoo", "Username: $userName")
        return@withContext "$userName"
    }

}