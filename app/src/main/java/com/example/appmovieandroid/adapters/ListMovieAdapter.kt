package com.example.appmovieandroid.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.ddd.androidutils.DoubleClick
import com.ddd.androidutils.DoubleClickListener
import com.example.appmovieandroid.MovieDetailActivity
import com.example.appmovieandroid.R
import com.example.appmovieandroid.databinding.BottomSheetDialogBinding
import com.example.appmovieandroid.databinding.ItemMovieBinding
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.models.Movie
import com.example.appmovieandroid.models.movie_detail.MovieDetail
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.Serializable

class  ListMovieAdapter(
    private val listMovie: List<Movie>,
    private val context: Context,
    private var isStayDetail: Boolean
) : RecyclerView.Adapter<ListMovieAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bindMovie(movie)

        holder.imageView.setOnClickListener {
            initBottomSheetDialog()
            bottomSheetDialog.show()
            bottomSheetDialog.setContentView(bottomSheetDialogLayout)
            bindingBottomSheetDialog.shimmerFrameLayout.startShimmerAnimation()
            CompanionObject.movieId = movie.movieId
            CompanionObject.categoryId = movie.categoryId
            CompanionObject.getMovieDetail {
                handleShowInfoMovieDetail(it)
                bindingBottomSheetDialog.shimmerFrameLayout.visibility = View.GONE
                bindingBottomSheetDialog.layoutDetail.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int = listMovie.size


    @SuppressLint("SetTextI18n")
    fun handleShowInfoMovieDetail(movieDetail: MovieDetail) {
        bindingBottomSheetDialog.apply {

            this.image.load(movieDetail.mainImage)
            this.title.text = movieDetail.name
            this.episodeCount.text =
                if (movieDetail.episodeCount > 0) "Episode Count: " + movieDetail.episodeCount.toString()
                else "Phim Lẻ"
            this.releaseYear.text = "Release: " + movieDetail.releaseYear
            this.score.text = movieDetail.score.toString()
            this.nation.text = "Nation: " + movieDetail.nation
            this.category.text = "Category: " + movieDetail.category.joinToString(", ")

            this.btnPlay.setOnClickListener {
                if (isStayDetail) {
                    (context as MovieDetailActivity).finish()
                }

                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("MovieDetail", movieDetail as Serializable)
                context.startActivity(intent)
                bottomSheetDialog.dismiss()

            }
        }
    }

    @SuppressLint("InflateParams")
    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialogLayout = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_dialog, null, false)
        bindingBottomSheetDialog = BottomSheetDialogBinding.bind(bottomSheetDialogLayout)
    }


}