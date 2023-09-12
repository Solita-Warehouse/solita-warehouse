package com.example.solitawarehouse

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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
        loginButton = rootView.findViewById(R.id.logoutButton)
        loginNameText = rootView.findViewById(R.id.loginNameText)
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
        return rootView
    }

}