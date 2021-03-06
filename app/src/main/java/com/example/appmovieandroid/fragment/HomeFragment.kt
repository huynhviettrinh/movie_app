package com.example.appmovieandroid.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.appmovieandroid.MovieDetailActivity
import com.example.appmovieandroid.R
import com.example.appmovieandroid.adapters.ItemCategoryAdapter
import com.example.appmovieandroid.databinding.BottomSheetDialogBinding
import com.example.appmovieandroid.databinding.FragmentHomeBinding
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.common.MoreFeature
import com.example.appmovieandroid.models.view_model.FragmentViewModel
import com.example.appmovieandroid.models.movie_detail.MovieDetail
import com.example.appmovieandroid.models.view_model.ConstViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val viewModel: FragmentViewModel by activityViewModels()
    private val imageSlider = ArrayList<SlideModel>()
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bindingBottomSheetDialog: BottomSheetDialogBinding
    private lateinit var bottomSheetDialogLayout: View
    private val scopeMain = CoroutineScope(Dispatchers.Main)
    lateinit var model: FragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()

        getListMovieCategory()
        getListMovieBanner()
        handleListenerEvent()
        return view
    }

    private fun handleListenerEvent() {
        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
        binding.avatarUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
        }
    }

    private fun getListMovieCategory() {
        viewModel.listMovieCategory.observe(viewLifecycleOwner) {
            binding.recyclerviewHome.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerviewHome.setHasFixedSize(true)

            binding.recyclerviewHome.adapter =
                firebaseAuth.uid?.let { it1 -> ItemCategoryAdapter(it, requireContext(), it1) }

        }

    }

    private fun getListMovieBanner() {
        viewModel.listBanner.observe(viewLifecycleOwner) { listFilm ->
            for (movie in listFilm) {
                imageSlider.add(SlideModel(movie.mainImage, ScaleTypes.FIT))
            }
            binding.imageSlider.setImageList(imageSlider)
            binding.imageSlider.setItemClickListener(object : ItemClickListener {
                override fun onItemSelected(position: Int) {
                    val movie = listFilm[position]
                    handleListenerEventClickBanner(movie.movieId, movie.categoryId)
                }
            })
        }
    }

    private fun handleListenerEventClickBanner(movieId: Int, categoryId: Int) {
        initBottomSheetDialog()
        bottomSheetDialog.show()
        bottomSheetDialog.setContentView(bottomSheetDialogLayout)
        bindingBottomSheetDialog.shimmerFrameLayout.startShimmerAnimation()
        CompanionObject.movieId = movieId
        CompanionObject.categoryId = categoryId
        CompanionObject.getMovieDetail {
            handleShowInfoMovieDetail(it)
            bindingBottomSheetDialog.shimmerFrameLayout.visibility = View.GONE
            bindingBottomSheetDialog.layoutDetail.visibility = View.VISIBLE

        }

    }


    @SuppressLint("SetTextI18n")
    private fun handleShowInfoMovieDetail(movieDetail: MovieDetail) {
        scopeMain.launch {
            bindingBottomSheetDialog.apply {
                this.image.load(movieDetail.mainImage)
                this.title.text = movieDetail.name
                this.episodeCount.text =
                    if (movieDetail.episodeCount > 0) "Episode Count: " + movieDetail.episodeCount.toString()
                    else "Phim L???"
                this.releaseYear.text = "Release: " + movieDetail.releaseYear
                this.score.text = movieDetail.score.toString()
                this.nation.text = "Nation: " + movieDetail.nation
                this.category.text = "Category: " + movieDetail.category.joinToString(", ")

                this.btnPlay.setOnClickListener {
                    val intent = Intent(context, MovieDetailActivity::class.java)
                    intent.putExtra("MovieDetail", movieDetail as Serializable)
                    context?.startActivity(intent)
                    bottomSheetDialog.dismiss()

                }
            }

        }
    }

    @SuppressLint("InflateParams")
    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_dialog, null, false)
        bindingBottomSheetDialog = BottomSheetDialogBinding.bind(bottomSheetDialogLayout)
    }





    override fun onStart() {
        super.onStart()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            Toast.makeText(requireContext(), "Welcome", Toast.LENGTH_LONG).show()
        } else {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
}