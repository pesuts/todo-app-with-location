package com.daniel.todoapp.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.daniel.todoapp.databinding.FragmentWelcomeBinding
import com.daniel.todoapp.ui.activity.MainActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intentDestination = Intent(requireContext(), MainActivity::class.java)
            intentDestination.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intentDestination)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            view.findNavController().navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
            )
        }

        binding.btnSignup.setOnClickListener {
            view.findNavController().navigate(
                WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment()
            )
        }
    }

}