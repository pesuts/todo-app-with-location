package com.daniel.todoapp.ui.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.FragmentLoginBinding
import com.daniel.todoapp.ui.activity.MainActivity
import com.daniel.todoapp.ui.fragment.ErrorDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()

            if(email.isBlank() || password.isBlank()) {
                setDrawable(binding.txtEmail, !email.isBlank())
                setDrawable(binding.txtPassword, !password.isBlank())
                showErrorDialog("All field must be filled!")
                return@setOnClickListener
            }

            if(!isValidEmail(email)){
                showErrorDialog("Email not valid!")
                setDrawable(binding.txtEmail, false)
                setDrawable(binding.txtPassword, !password.isBlank())
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intentDestination = Intent(requireContext(), MainActivity::class.java)
                        intentDestination.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intentDestination)
                    } else {
                        showErrorDialog("Login failed!")
                    }
                }

        }

        binding.btnSignUp.setOnClickListener{
            view.findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            )
        }
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun setDrawable(editText: EditText, status: Boolean){
        if(status) editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border)
        else editText.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_border_red)
    }

    fun showErrorDialog(message: String){
        val args = Bundle()
        val key = "MESSAGE"
        args.putString(key, message)

        val mDialogFragment = ErrorDialogFragment()
        val mFragmentManager = childFragmentManager
        mDialogFragment.arguments = args
        mDialogFragment.show(mFragmentManager, ErrorDialogFragment::class.java.simpleName)
    }

}