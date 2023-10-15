package data

import android.util.Log
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class OdooDatabaseConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val commonConfig = XmlRpcClientConfigImpl()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val authService = AuthenticationService(baseUrl)

    init {
        commonConfig.serverURL = URL("http://10.0.2.2:8069/xmlrpc/2/common")
        modelConfig.serverURL = URL("http://10.0.2.2:8069/xmlrpc/2/object")
    }

    fun authenticate(): Any? {
        return authService.authenticate(db, username, password)
    }

    fun returnUserData(): String {
        val auth = authenticate()
        if (auth is Boolean) {
            Log.i("odoo", "returnUserData func cannot be ran!")
            return ""
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
        return "$userName"
    }

}