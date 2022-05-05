package com.example.appmovieandroid.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appmovieandroid.R
import com.example.appmovieandroid.common.MoreFeature
import com.example.appmovieandroid.databinding.FragmentLoginBinding
import com.example.appmovieandroid.models.view_model.ConstViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var email: TextInputEditText
    private lateinit var pass: TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var model: ConstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)
        initView()

        firebaseAuth = FirebaseAuth.getInstance()


        handleListenerEventView()
        return view
    }


    private fun handleValidate(): Boolean {
        if (email.text?.isEmpty() == true) {
            email.error = "Email is required!";
            email.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches()) {
            email.error = "Please provide valid email";
            email.requestFocus()
            return false
        }

        if (pass.text?.isEmpty() == true) {
            pass.error = "Email is required!";
            pass.requestFocus()
            return false
        }

        if (pass.text.toString().length < 6) {
            pass.error = "Min password length should be 6 characters!";
            pass.requestFocus()
            return false
        }
        return true
    }

    private fun handleListenerEventView() {
        binding.btnLogin.setOnClickListener {
            if (handleValidate()) {
                firebaseAuth.signInWithEmailAndPassword(
                    email.text.toString().trim(),
                    pass.text.toString().trim()
                )
                    .addOnCompleteListener {
                        MoreFeature.isHandleStoreListFavorite = true
                        model = ViewModelProvider(requireActivity()).get(ConstViewModel::class.java)

                        // Store uid view model
                        firebaseAuth.currentUser?.let { it1 -> model.setUidUser(it1.uid) }

                        // Store list movie favorite view model
                        firebaseAuth.currentUser?.let { MoreFeature.uid = it.uid }
                        MoreFeature.listMovieFavoriteByUid { listMovieFavorite ->
                            model.setMovieFavorite(listMovieFavorite)
                        }


                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    .addOnFailureListener { e ->
                        Log.v("VVV", e.toString())
                        Toast.makeText(
                            requireContext(), e.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        binding.textRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.forgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassFragment)

        }
    }

    private fun initView() {
        email = binding.emailEditText
        pass = binding.passwordEditText
    }


}