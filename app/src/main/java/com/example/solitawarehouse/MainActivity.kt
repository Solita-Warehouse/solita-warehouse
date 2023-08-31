package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import model.User

class MainActivity : ComponentActivity() {
    private lateinit var mainTitle : TextView
    private lateinit var fullName : EditText
    private lateinit var department : EditText
    private lateinit var eMail : EditText
    private lateinit var loginButton : Button
    private lateinit var loginNameText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainTitle = findViewById(R.id.mainTitle)
        fullName = findViewById(R.id.fullName)
        department = findViewById(R.id.department)
        eMail = findViewById(R.id.eMail)
        loginButton = findViewById(R.id.loginButton)
        loginNameText = findViewById(R.id.loginNameText)
        var inputFullName = ""
        var inputDepartment = ""
        var inputEmail = ""

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

        loginButton.setOnClickListener() {
            val user = User(inputFullName, inputDepartment, inputEmail)
            loginNameText.text = "Hello $user"
        }

    }

}
