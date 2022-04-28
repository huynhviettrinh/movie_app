package com.example.appmovieandroid.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.appmovieandroid.R
import com.example.appmovieandroid.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var passConf: EditText
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        binding = FragmentRegisterBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()

        initView()
        handleListenerEventView()
        return view
    }


    private fun handleValidate(): Boolean {
        if (email.text?.isEmpty() == true) {
            email.error = "Email is required!"
            email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please provide valid email"
            email.requestFocus()
            return false
        }

        if (pass.text?.isEmpty() == true) {
            pass.error = "Email is required!"
            pass.requestFocus()
            return false
        }

        if (pass.text.toString().length < 6) {
            pass.error = "Min password length should be 6 characters!";
            pass.requestFocus()
            return false
        }

        if (passConf.text?.isEmpty() == true) {
            passConf.error = "Email is required!"
            passConf.requestFocus()
            return false
        }

        if (passConf.text.toString() != pass.text.toString()) {
            passConf.error = "Password is not matching"
            passConf.requestFocus()
            return false
        }
        return true
    }

    private fun handleListenerEventView() {
        binding.btnRegister.setOnClickListener {
            if (handleValidate()) {
                firebaseAuth.createUserWithEmailAndPassword(
                    email.text.toString().trim(),
                    pass.text.toString().trim()
                )
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                        } else {
                            Log.v("VVV", it.exception.toString())
                            Toast.makeText(
                                requireContext(), it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.btnBackLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun initView() {
        email = binding.emailEditText
        pass = binding.passwordEditText
        passConf = binding.passwordConfirm
    }


}