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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.solita_warehouse.R
import model.User
import viewmodels.UserView

class ReportsFragment : Fragment() {
    private lateinit var userView: UserView
    private lateinit var button : Button
    private lateinit var name : EditText
    private lateinit var department : EditText
    private lateinit var email : EditText
    private lateinit var button2 : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_reports, container, false)

        userView = ViewModelProvider(this).get(UserView::class.java)
        button = rootView.findViewById(R.id.button)
        name = rootView.findViewById(R.id.ptName)
        department = rootView.findViewById(R.id.ptDepartment)
        email = rootView.findViewById(R.id.ptEmail)
        button2 = rootView.findViewById(R.id.button2)

        var inputName = ""
        var inputDepartment = ""
        var inputEmail = ""

        val user = User("Jukka", "Boss", "jukka@jukka.com")

        userView.setUser(user)

        userView.user.observe(viewLifecycleOwner, Observer { user ->
            // Update UI with user data
            // For example, set the text of TextViews or populate a user profile screen
            val userNameTextView = rootView.findViewById<TextView>(R.id.tvName)
            val departmentTextView = rootView.findViewById<TextView>(R.id.tvDepartment)
            val emailTextView = rootView.findViewById<TextView>(R.id.tvEmail)

            userNameTextView.text = user?.userName
            departmentTextView.text = user?.department
            emailTextView.text = user?.eMail
        })

        button.setOnClickListener {
            userView.deleteUser()
        }

        name.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputName = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        department.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputDepartment = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        email.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputEmail = "$p0"
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        button.setOnClickListener {
            userView.deleteUser()
        }

        button2.setOnClickListener {
            val newUser = User(inputName, inputDepartment, inputEmail)
            userView.setUser(newUser)
        }
        return rootView
    }

}