package com.example.appmovieandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmovieandroid.R
import com.example.appmovieandroid.adapters.ListMovieAdapter
import com.example.appmovieandroid.databinding.FragmentRecommendBinding
import com.example.appmovieandroid.models.view_model.FragmentViewModel

class RecommendFragment : Fragment() {
    private val viewModel: FragmentViewModel by activityViewModels()
    private lateinit var binding: FragmentRecommendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recommend, container, false)
        binding = FragmentRecommendBinding.bind(view)

        viewModel.recommend.observe(viewLifecycleOwner) {
            binding.recommendRV.setHasFixedSize(true)
            binding.recommendRV.layoutManager = GridLayoutManager(requireContext(),3)
            binding.recommendRV.adapter = ListMovieAdapter(it,requireContext(),true)
        }
        return view
    }


}