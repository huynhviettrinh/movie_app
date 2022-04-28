package com.example.appmovieandroid.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmovieandroid.R
import com.example.appmovieandroid.adapters.ListMovieAdapter
import com.example.appmovieandroid.databinding.FragmentListFavoriteMovieBinding
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.view_model.ConstViewModel

class ListFavoriteMovieFragment : Fragment() {
    private lateinit var binding: FragmentListFavoriteMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_favorite_movie, container, false)
        binding = FragmentListFavoriteMovieBinding.bind(view)
        setThemeUI ()
        getListMovieFavorite()
        handleListenerEvent()

        return view
    }

    private fun setRecyclerview(listMovie: List<Movie>) {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerView.adapter = ListMovieAdapter(listMovie, requireContext(), false)
    }

    private fun handleListenerEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_listFavoriteMovieFragment_to_accountFragment)

        }
    }

    private fun getListMovieFavorite() {
        val model = ViewModelProvider(requireActivity()).get(ConstViewModel::class.java)
        model.listMovieFavorite.observe(viewLifecycleOwner) {
            setRecyclerview(it)
        }
    }

    private fun setThemeUI () {
        binding.textView.setTextColor(resources.getColor(R.color.card_bg_color))
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        binding.btnBack.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
    }

}
