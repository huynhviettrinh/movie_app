package com.example.appmovieandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appmovieandroid.R
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.common.MoreFeature
import com.example.appmovieandroid.databinding.FragmentAccountBinding
import com.example.appmovieandroid.models.view_model.ConstViewModel
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        binding = FragmentAccountBinding.bind(view)

        firebaseAuth = FirebaseAuth.getInstance()

        handleListenerEvent()
        setLayout()
        return view
    }

    private fun handleListenerEvent() {
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_accountFragment_to_loginFragment)

        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
        }

        binding.myList.setOnClickListener {
            callApiStoreListMovieFavoriteViewModel()
            findNavController().navigate(R.id.action_accountFragment_to_listFavoriteMovieFragment)
        }

    }

    private fun setLayout() {
        binding.email.text = firebaseAuth.currentUser?.email ?: ""
    }

    private fun callApiStoreListMovieFavoriteViewModel() {
        val model = ViewModelProvider(requireActivity()).get(ConstViewModel::class.java)
        firebaseAuth.currentUser?.let { MoreFeature.uid = it.uid }
        MoreFeature.listMovieFavoriteByUid { listMovieFavorite ->
            model.setMovieFavorite(listMovieFavorite)
        }
    }

}