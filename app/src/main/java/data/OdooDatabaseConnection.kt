package data

import android.util.Log
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class OdooDatabaseConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val commonConfig = XmlRpcClientConfigImpl()
    private val modelConfig = XmlRpcClientConfigImpl()

    init {
        commonConfig.serverURL = URL("http://10.0.2.2:8069/xmlrpc/2/common")
        modelConfig.serverURL = URL("http://10.0.2.2:8069/xmlrpc/2/object")
    }

    fun authenticate(): Int {
        val auth = client.execute(commonConfig, "authenticate", listOf(db, username, password, emptyMap<String, Any>())) as Int
        Log.i("odoo", "$auth")
        return auth
    }

    fun returnUserData(): String {
        val userInformation = client.execute(
            modelConfig,
            "execute_kw",
            listOf(
                db, authenticate(), password, // Use the authenticate method to get the user ID
                "res.users", "read",
                listOf(authenticate()), // Pass the user's uid to read their information
                mapOf("fields" to listOf("login")) // Specify the fields you want to retrieve, in this case, the "login" field
            )
        ) as Array<*>

        // Extract the username from the user information
        val userName = userInformation[0]
        Log.i("odoo", "Username: $userName")
        return "$userName"
    }

}