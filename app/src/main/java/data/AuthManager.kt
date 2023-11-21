package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.EnvVariableLoader
import model.User
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class AuthManager private constructor() {

    companion object {
        private var instance: AuthManager? = null

        fun getInstance(): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager().also { instance = it }
            }
        }
    }

    // Data to be provided to the app
    private var uid: Any? = null
    private var userPassword: String? = null
    private var currentUser: User? = null

    private val URL = EnvVariableLoader.URL
    private val DB = EnvVariableLoader.DB
    private val URL_LOCAL = EnvVariableLoader.URL_LOCAL
    private val commonConfig = XmlRpcClientConfigImpl()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val client = XmlRpcClient()
    private var success: Boolean = false

    suspend fun authSequence(username: String, password: String) :Boolean{
        Log.d("odoo", "authSequence started")
        Log.d("odoo", "authSequence : authenticateUser()")
        authenticateUser(username, password)
        Log.d("odoo", "authSequence : fetchUserData()")
        fetchUserData()
        return success
    }
    suspend fun authenticateUser(username: String, password: String){
        withContext(Dispatchers.IO) {
            try {
                commonConfig.serverURL = URL("$URL/xmlrpc/2/common")
                userPassword = password
                uid = client.execute(
                    commonConfig,
                    "authenticate",
                    listOf(DB, username, password, emptyMap<String, Any>())
                )
                if(uid == false){
                    success = false
                    Log.e("odoo", "FAILED authenticateUser(): authenticate returned ${uid}")
                }
                else{
                    success = true
                    Log.d("odoo", "SUCCESS authenticateUser(): $uid")
                }
            } catch (e: Exception) {
                success = false
                Log.e("odoo", "FAILED authenticateUser(): ${e.message}"/*, e*/)
                // Handle the exception as needed (e.g., show an error message, log the error, etc.)
            }
        }
    }
    suspend fun fetchUserData() {
        withContext(Dispatchers.IO) {
            try {
                if(uid == false || uid == null){
                    success = false
                    Log.e("odoo", "BYPASSED fetchUserData(): no valid UID")
                    return@withContext
                }
                modelConfig.serverURL = URL("$URL/xmlrpc/2/object")
                val userInformation = client.execute(
                    modelConfig,
                    "execute_kw",
                    listOf(
                        DB, uid, userPassword,
                        "res.users", "read",
                        listOf(uid),
                        mapOf("fields" to listOf("login", "partner_id"))
                    )
                ) as Array<*>

                currentUser = User.createUserFromData(userInformation)
                Log.i("odoo", currentUser.toString())
                success = true
                Log.i("odoo", "SUCCESS fetchUserData(): ${userInformation[0]}")
            } catch (e: Exception) {
                success = false
                Log.e("odoo", "FAILED in fetchUserData(): ${e.message}")
                // Handle the exception as needed (e.g., show an error message, log the error, etc.)
            }
        }
    }
    fun logout(){
        currentUser = null
        uid = null
        userPassword = null
    }
    fun getUid(): Any? {
        Log.d("odoo", "UID current value when got ${uid}");
        return uid
    }
    fun getPassword(): String? {
        return userPassword
    }
    fun getCurrentUser(): User{
        if(currentUser == null){
            return User("", 0, 0, "")
        }
        else{
            return currentUser as User
        }
    }

    fun isLogged(): Boolean{
        if(currentUser == null){
            return false
        }
        return true
    }
}
