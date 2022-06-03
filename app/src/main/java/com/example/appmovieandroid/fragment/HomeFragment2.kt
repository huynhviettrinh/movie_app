package com.example.appmovieandroid.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmovieandroid.R
import com.example.appmovieandroid.adapters.ItemCategoryAdapter
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.databinding.FragmentHome2Binding
import com.example.appmovieandroid.databinding.FragmentHomeBinding
import com.example.appmovieandroid.models.MovieCategory
import com.google.firebase.auth.FirebaseAuth

class HomeFragment2 : Fragment() {
    private lateinit var binding: FragmentHome2Binding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home2, container, false)
        binding = FragmentHome2Binding.bind(view)

        firebaseAuth = FirebaseAuth.getInstance()

        callCategoryMovieApiData()


        return view
    }


    private fun callCategoryMovieApiData() {
        CompanionObject.getListMovie { listCategory: List<MovieCategory> ->
            binding.recyclerviewHome.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerviewHome.setHasFixedSize(true)

            Log.v("VVV","callCategoryMovieApiData")

            binding.recyclerviewHome.adapter =
                firebaseAuth.uid?.let { it1 -> ItemCategoryAdapter(listCategory, requireContext(), it1,true) }

        }
    }

}