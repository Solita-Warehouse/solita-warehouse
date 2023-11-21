package com.example.solitawarehouse

import android.app.AlertDialog
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
import com.example.solita_warehouse.R
import data.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.EnvVariableLoader

class LoginFragment : Fragment() {
    private lateinit var mainTitle : TextView
    private lateinit var fullName : EditText
    private lateinit var department : EditText
    private lateinit var eMail : EditText
    private lateinit var loginButton : Button
    private lateinit var logoutButton : Button
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
        logoutButton = rootView.findViewById(R.id.logoutButton)

        var inputFullName = ""
        var inputDepartment = ""
        var inputEmail = ""
        val URL = EnvVariableLoader.URL
        val DB = EnvVariableLoader.DB
        val URL_LOCAL = EnvVariableLoader.URL_LOCAL

        // Check your condition here
        val authManager = AuthManager.getInstance()
        val isLoggedIn = authManager.isLogged()/* your condition to check if the user is logged in */

        // Update UI based on the condition
        if (isLoggedIn) {
            // If the user is logged in, hide the input fields and show a welcome message
            fullName.visibility = View.GONE
            department.visibility = View.GONE
            eMail.visibility = View.GONE
            loginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            loginNameText.text = "Logged as ${authManager.getCurrentUser().userName}"
        } else {
            // If the user is not logged in, show the input fields and login button
            fullName.visibility = View.VISIBLE
            department.visibility = View.VISIBLE
            eMail.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            loginNameText.text = ""
        }

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

        loginButton.setOnClickListener {
            Log.d("odoo", "### AUTHENTICATION ###");
            CoroutineScope(Dispatchers.Main).launch {
                val success = AuthManager.getInstance().authSequence(inputFullName, inputEmail)
                // Show alert after that
                showAlert("Authentication", "Authentication success : $success")
                updateUIAfterLogin()
            }
        }

        // Set click listener for the logout button
        logoutButton.setOnClickListener {
            AuthManager.getInstance().logout()
            // After logging out, update UI
            updateUIAfterLogout()
        }

        return rootView
    }
    private fun updateUIAfterLogin() {
        val authManager = AuthManager.getInstance()
        val isLoggedIn = authManager.isLogged()
        if (isLoggedIn) {
            // If the user is logged in, hide the input fields and show a welcome message
            fullName.visibility = View.GONE
            department.visibility = View.GONE
            eMail.visibility = View.GONE
            loginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            loginNameText.text = "Logged as ${authManager.getCurrentUser().userName}"
        } else {
            // If the user is not logged in, show the input fields and login button
            fullName.visibility = View.VISIBLE
            department.visibility = View.VISIBLE
            eMail.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            loginNameText.text = ""
        }
    }

    private fun updateUIAfterLogout() {
        // After logging out, hide welcome and logout, show input fields and login button
        fullName.visibility = View.VISIBLE
        department.visibility = View.VISIBLE
        eMail.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
        logoutButton.visibility = View.GONE
        loginNameText.text = ""
    }

    private fun showAlert(title: String, message : String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                // Do something when the "OK" button is clicked, if needed
            }
            .create()

        alertDialog.show()
    }
}