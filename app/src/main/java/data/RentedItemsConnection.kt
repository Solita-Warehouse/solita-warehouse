package data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.EnvVariableLoader
import model.Item
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.io.EOFException
import java.net.URL

class RentedItemsConnection() {
    private val client = XmlRpcClient()
    private val modelConfig = XmlRpcClientConfigImpl()
    private val itemList = mutableListOf<Item>()
    val DB = EnvVariableLoader.DB
    val PASSWORD = EnvVariableLoader.PASSWORD
    val PASSWORD_LOCAL = EnvVariableLoader.PASSWORD_LOCAL
    val URL = EnvVariableLoader.URL
    val LOCAL_URL = EnvVariableLoader.URL_LOCAL

    init {
        modelConfig.serverURL = URL("${URL}/xmlrpc/2/object")
    }


    suspend fun returnRentedItems(): MutableList<Item> = withContext(Dispatchers.IO) {
        val itemsList : Array<*>;

        try {
            val rentalOrderSearch = client.execute(
                modelConfig,
                "execute_kw",
                listOf(
                    DB, 2, PASSWORD,
                    "sale.order", "search_read",
                    listOf(emptyList<Any>()),
                    mapOf("fields" to listOf("name", "partner_id", "state"))


                )
            ) as Array<*>

            for(data in rentalOrderSearch){
                for(item in data as Map<*,*>){
                    if(item.value is Array<*>){
                        print("${item.key}=[")
                        for(subItem in item.value as Array<*>){
                            print("($subItem)")
                        }
                        println("]")
                    }
                    else{
                        println(item)
                    }
                }
                println()
            }

        } catch(e: Exception) {
            Log.i("odoo", "crashed :( $e")
        }

        return@withContext mutableListOf();
    }
}