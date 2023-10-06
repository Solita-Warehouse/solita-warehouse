package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.example.solita_warehouse.R
import org.apache.xmlrpc.client.XmlRpcClient
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl
import java.net.URL

class LoginFragment : Fragment() {
    private lateinit var mainTitle : TextView
    private lateinit var fullName : EditText
    private lateinit var department : EditText
    private lateinit var eMail : EditText
    private lateinit var loginButton : Button
    private lateinit var loginNameText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_login, container, false)
        mainTitle = rootView.findViewById(R.id.mainTitle)
        fullName = rootView.findViewById(R.id.fullName)
        department = rootView.findViewById(R.id.department)
        eMail = rootView.findViewById(R.id.eMail)
        loginButton = rootView.findViewById(R.id.LoginButton)
        loginNameText = rootView.findViewById(R.id.loginNameText)
        var inputFullName = ""
        var inputDepartment = ""
        var inputEmail = ""
        val url = "http://10.0.2.2:8069" //=> URL to call localhost from the emulator
        val db = "db"
        val username = "admin"
        val password = "admin"

        fullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputFullName = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        department.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputDepartment = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        eMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputEmail = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        loginButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Log.d("odoo", "### AUTHENTICATION ###");

                println("### AUTHENTICATION ###")
                val client = XmlRpcClient()
                val commonConfig = XmlRpcClientConfigImpl()

                /**
                 * Set the connection data for this config.
                 * Here we use http://localhost:8069 + /xmlrpc/2/common
                 * "The xmlrpc/2/common endpoint provides meta-calls which don’t require authentication,
                 * such as the authentication itself or fetching version information." -odoo.com
                 */
                commonConfig.serverURL = URL("$url/xmlrpc/2/common")

                // Fetching the server version is a good way to test if the connection information is correct
                val serverVersion = client.execute(commonConfig, "version", emptyList<Any>())
                //println("Server information: $serverVersion")
                Log.d("odoo", "Server information: $serverVersion");

                // Authentication
                val uid = client.execute(commonConfig, "authenticate", listOf(db, username, password, emptyMap<String, Any>())) as Int
                println("Authenticated user id: $uid")
                Log.d("odoo", "Authenticated user id: $uid");
                /**
                 * READ - Fetching Model's Data
                 *
                 * Set up client and config.
                 * connection string for this config : http://localhost:8069 + xmlrpc/2/object
                 * "The second endpoint is xmlrpc/2/object. It is used to
                 * call methods of odoo models via the execute_kw RPC function." -odoo.com
                 */
                println("\r\n\r\n### READ - Fetching user's name ###")
                val models = XmlRpcClient()
                val modelConfig = XmlRpcClientConfigImpl()
                modelConfig.serverURL = URL("$url/xmlrpc/2/object")
                /**
                 * The result from the query is stored in an array (authenticatedUser)
                 */

                /** Currently causes problems to the code
                val authenticatedUser = models.execute(
                    modelConfig,
                    "execute_kw",
                    listOf(
                        db, uid, password,
                        "res.users", "read",
                        listOf(arrayOf(2,3)),
                        mapOf("fields" to listOf("partner_id", "login"))
                    )
                ) as Array<*>
                */

                /**
                 * SEARCH - Returning the ID for all the entry of a table that fits search parameters
                 *///
                println("### SEARCH - All partner whose name start with D ###")

                val partnerNameStartWithID =  models.execute(
                    modelConfig,
                    "execute_kw",
                    listOf(
                        db, uid, password,
                        "res.partner", "search",
                        listOf(
                            listOf(
                                listOf("name", "like", "S%")
                                //listOf("active", "=", false)

                            )
                        )
                    ))as Array<*>

                for(id in partnerNameStartWithID){
                    print("${id} ")
                    Log.d("odoo", id.toString());

                }
                println()

                val partnerNameStartWith = models.execute(
                    modelConfig,
                    "execute_kw",
                    listOf(
                        db, uid, password,
                        "res.partner", "read",
                        listOf(partnerNameStartWithID),
                        mapOf("fields" to listOf( "name"))
                    )
                ) as Array<*>

                for(partner in partnerNameStartWith){
                    println(partner)
                    Log.d("odoo", partner.toString());
                }
            }

        })


        return rootView
    }

}