package com.example.appmovieandroid.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.example.appmovieandroid.MovieDetailActivity
import com.example.appmovieandroid.R
import com.example.appmovieandroid.databinding.BottomSheetDialogBinding
import com.example.appmovieandroid.databinding.ItemMovieBinding
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.common.MoreFeature
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.post_data_server.PostMovieFavorite
import com.example.appmovieandroid.models.movie_detail.MovieDetail
import com.example.appmovieandroid.models.view_model.ConstViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.io.Serializable


class ItemMovieAdapter(
    private val listMovie: List<Movie>,
    private val context: Context,
    private val uid: String
) : RecyclerView.Adapter<ItemMovieAdapter.ViewHolder>() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bindingBottomSheetDialog: BottomSheetDialogBinding
    private lateinit var bottomSheetDialogLayout: View
    private val scopeMain = CoroutineScope(Dispatchers.Main)

    class ViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val imageView = binding.imageView

        fun bindMovie(movie: Movie) {
            Glide.with((binding.root))
                .load(movie.mainImage)
                .placeholder(R.drawable.animation_loading)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bindMovie(movie)
        initBottomSheetDialog()

        val doubleClick = DoubleClick(object : DoubleClickListener {
            override fun onSingleClickEvent(view: View?) {
                bottomSheetDialog.show()
                bottomSheetDialog.setContentView(bottomSheetDialogLayout)
                bindingBottomSheetDialog.shimmerFrameLayout.startShimmerAnimation()

                CompanionObject.movieId = movie.movieId
                CompanionObject.categoryId = movie.categoryId
                CompanionObject.getMovieDetail {
                    handleShowInfoMovieDetail(it)
                    callApiPostAddIntoListMovie(it, checkExist = true)
                    bindingBottomSheetDialog.layoutDetail.visibility = View.VISIBLE
                    bindingBottomSheetDialog.shimmerFrameLayout.visibility = View.GONE
                }
            }

            override fun onDoubleClickEvent(view: View?) {
                scopeMain.launch {
                    bottomSheetDialog.show()
                    bottomSheetDialog.setContentView(bottomSheetDialogLayout)
                    bindingBottomSheetDialog.shimmerFrameLayout.startShimmerAnimation()
                    delay(400L)
                    bottomSheetDialog.dismiss()

                }

            }
        })

        holder.imageView.setOnClickListener(doubleClick)


    }

    override fun getItemCount(): Int = listMovie.size

    @SuppressLint("SetTextI18n")
    private fun handleShowInfoMovieDetail(movieDetail: MovieDetail) {
        bindingBottomSheetDialog.apply {
            Glide.with((this.root))
                .load(movieDetail.mainImage)
                .placeholder(R.drawable.animation_loading)
                .into(this.image)
            this.title.text = movieDetail.name
            this.episodeCount.text =
                if (movieDetail.episodeCount > 0) "Episode Count: " + movieDetail.episodeCount.toString()
                else "Phim Lẻ"
            this.releaseYear.text = "Release: " + movieDetail.releaseYear
            this.score.text = movieDetail.score.toString()
            this.nation.text = "Nation: " + movieDetail.nation
            this.category.text = "Category: " + movieDetail.category.joinToString(", ")

            this.btnPlay.setOnClickListener {
                intentMovieDetailActivity(movieDetail)
                bottomSheetDialog.dismiss()
            }

            this.btnAddIntoList.setOnClickListener {
                callApiPostAddIntoListMovie(movieDetail, false)
            }

            this.btnRemove.setOnClickListener {
                callApiPostAddIntoListMovie(movieDetail, false)

            }

        }
    }

    private fun callApiPostAddIntoListMovie(movieDetail: MovieDetail, checkExist: Boolean) {
        MoreFeature.body = PostMovieFavorite(
            uid,
            movieDetail.movieId,
            movieDetail.categoryId,
            movieDetail.mainImage,
            movieDetail.name,
            checkExist,
        )

        MoreFeature.addMovieIntoListFavorite {
            hideAndVisible(it.status)
        }
    }

    private fun hideAndVisible(check: Int) {
        if (check == 1) {
            bindingBottomSheetDialog.btnAddIntoList.visibility = View.VISIBLE
            bindingBottomSheetDialog.btnRemove.visibility = View.GONE
        } else {
            bindingBottomSheetDialog.btnRemove.visibility = View.VISIBLE
            bindingBottomSheetDialog.btnAddIntoList.visibility = View.GONE
        }
    }

    @SuppressLint("InflateParams")
    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_dialog, null, false)
        bindingBottomSheetDialog = BottomSheetDialogBinding.bind(bottomSheetDialogLayout)
    }


    private fun intentMovieDetailActivity(movieDetail: MovieDetail) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra("MovieDetail", movieDetail as Serializable)
        context.startActivity(intent)
    }


}