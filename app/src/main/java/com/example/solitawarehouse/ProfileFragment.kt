package com.example.solitawarehouse

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import model.User


class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val logoutButton = rootView.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener() {
            Log.i("", "moi")
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

        return rootView

    }

}