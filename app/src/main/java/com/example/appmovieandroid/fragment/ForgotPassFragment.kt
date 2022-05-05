package com.example.appmovieandroid.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.appmovieandroid.R
import com.example.appmovieandroid.databinding.FragmentForgotPassBinding
import com.example.appmovieandroid.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPassFragment : Fragment() {
    lateinit var binding: FragmentForgotPassBinding
    private lateinit var email: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_pass, container, false)
        binding = FragmentForgotPassBinding.bind(view)
        initView()

        firebaseAuth = FirebaseAuth.getInstance()

        handleListenerEventView()
        return view
    }

    private fun handleListenerEventView() {
        binding.submitForgotPass.setOnClickListener {
            when (handleValidate()) {
                0 -> {
                    Toast.makeText(requireContext(), "Email is required!", Toast.LENGTH_LONG).show()
                }
                1 -> {
                    Toast.makeText(
                        requireContext(),
                        "Please provide valid email",
                        Toast.LENGTH_LONG
                    ).show()
                }
                2 -> {
                    firebaseAuth.sendPasswordResetEmail(email.text.toString().trim())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(requireContext(), "Check email to reset password!", Toast.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_forgotPassFragment_to_loginFragment)
                            } else {
                                Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_LONG).show()

                            }

                        }
                }
            }

        }
    }

    private fun handleValidate(): Int {
        if (email.text?.isEmpty() == true) {
            email.requestFocus()
            return 0
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches()) {
            email.requestFocus()
            return 1
        }

        return 2
    }

    private fun initView() {
        email = binding.emailEditText

    }


}