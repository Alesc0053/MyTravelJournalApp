package com.example.mytraveljournal.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mytraveljournal.LoginActivity
import com.example.mytraveljournal.R
import com.example.mytraveljournal.ui.home.SettingsUserViewModel

class Profile : Fragment() {

    private lateinit var mSettingsUserViewModel: SettingsUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        mSettingsUserViewModel = ViewModelProvider(requireActivity())[SettingsUserViewModel::class.java]


        val username = view.findViewById<TextView>(R.id.usernameProfile)
        username.text = mSettingsUserViewModel.getUsername()

        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener{
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        return view;
    }

}