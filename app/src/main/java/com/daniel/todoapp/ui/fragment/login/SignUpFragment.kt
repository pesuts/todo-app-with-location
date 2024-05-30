package com.daniel.todoapp.ui.fragment.login

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
import com.daniel.todoapp.databinding.FragmentSignUpBinding
import com.daniel.todoapp.ui.fragment.ErrorDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth


class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.btnSignUp.isEnabled = false
        binding.btnSignUp.alpha = 0.95f

        binding.checkBox.setOnClickListener{
            binding.btnSignUp.isEnabled = binding.checkBox.isChecked
            if(binding.checkBox.isChecked) {
                binding.btnSignUp.alpha = 1.0f
            } else binding.btnSignUp.alpha = 0.95f
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val name = binding.txtName.text.toString()
            val password = binding.txtPassword.text.toString()
            val rePassword = binding.txtPasswordRetype.text.toString()
            var checkbox = binding.checkBox.isChecked

            if(email.isBlank() || name.isBlank() || password.isBlank() || rePassword.isBlank()) {
                setDrawable(binding.txtEmail, !email.isBlank())
                setDrawable(binding.txtName, !name.isBlank())
                setDrawable(binding.txtPassword, !password.isBlank())
                setDrawable(binding.txtPasswordRetype, !rePassword.isBlank())

                showErrorDialog("All field must be filled!")
                return@setOnClickListener
            }

            if(!isValidEmail(email)){
                showErrorDialog("Email not valid!")
                setDrawable(binding.txtEmail, false)
                setDrawable(binding.txtName, !name.isBlank())
                setDrawable(binding.txtPassword, !password.isBlank())
                setDrawable(binding.txtPasswordRetype, !rePassword.isBlank())
                return@setOnClickListener
            }

            if(!checkbox){
                showErrorDialog("You must agree the condition!")
                return@setOnClickListener
            }

            if(password != rePassword){
                showErrorDialog("Password not match!")
                setDrawable(binding.txtEmail, !email.isBlank())
                setDrawable(binding.txtName, !name.isBlank())
                setDrawable(binding.txtPassword, false)
                setDrawable(binding.txtPasswordRetype, false)

                binding.txtPassword.setText("")
                binding.txtPasswordRetype.setText("")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        view.findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                        )
                    } else {
                        task.exception?.localizedMessage?.let { it1 -> showErrorDialog(it1) }
                    }
                }

        }

        binding.btnLogin.setOnClickListener {
            view.findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
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