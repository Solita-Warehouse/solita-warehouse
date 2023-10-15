package data

import android.util.Log
import org.apache.xmlrpc.XmlRpcException
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfig
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class AuthenticationService(private val baseUrl: String) {
    private val commonConfig = XmlRpcClientConfigImpl()

    init{
        commonConfig.serverURL = URL("$baseUrl/xmlrpc/2/common")
    }

    fun authenticate(db: String, username: String, password: String): Any? {
        val client = XmlRpcClient()
        val auth = client.execute(commonConfig, "authenticate", listOf(db, username, password,
            emptyMap<String,Any>()))

        if (auth is Int) {
            Log.i("odoo", "User auth id: $auth")
        } else {
            Log.i("odoo", "Authentication failed: $auth")
        }
        return auth
    }

}