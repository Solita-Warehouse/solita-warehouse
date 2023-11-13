package data

import android.util.Log
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.User

//BAD CONVENTION. Just for testing purposes exposed like this atm.
var currentUser = User("", 0, 0, "")

class LoginConnection(private val baseUrl: String, private val db: String, private val username: String, private val password: String) {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val authService = AuthenticationService(baseUrl)

    init {
        modelConfig.serverURL = URL("$baseUrl/xmlrpc/2/object")
    }

    suspend fun returnUserData(): User = withContext(Dispatchers.IO) {
        try {
            val auth = authService.authenticate(db, username, password)
            if (auth is Boolean) {
                Log.i("odoo", "returnUserData func cannot be run!")
                return@withContext currentUser
            }

            val userInformation = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    db, auth, password,
                    "res.users", "read",
                    listOf(auth),
                    mapOf("fields" to listOf("login", "partner_id"))
                )
            ) as Array<*>

            for (user in userInformation) {
                for (item in user as Map<*, *>) {
                    if (item.key == "id") {
                        currentUser.id = item.value as Int
                    }
                    if (item.key == "login") {
                        currentUser.userName = item.value.toString()
                    }
                    if (item.key == "partner_id") {
                        if (item.value is Array<*>) {
                            for (subItem in item.value as Array<*>) {
                                if (subItem is Int) {
                                    currentUser.partnerId = subItem
                                }
                            }
                        }
                    }
                }
            }
            Log.i("odoo", currentUser.toString())

            return@withContext currentUser
        } catch (e: Exception) {
            // Handle exceptions as needed
            Log.i("odoo", "Error in LoginConnection class: ${e.printStackTrace()}")
            return@withContext currentUser // Return an empty string or handle the error case as appropriate
        }
    }

    suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
        return@withContext currentUser
    }

}