package com.example.appmovieandroid.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovieandroid.MovieDetailActivity
import com.example.appmovieandroid.databinding.ItemEpisodeBinding
import com.example.appmovieandroid.common.CompanionObject
import com.example.appmovieandroid.models.movie_detail.EpisodeDetail
import com.example.appmovieandroid.models.movie_detail.EpisodeDetail2
import com.example.appmovieandroid.models.movie_detail.Subtitle


class EpisodeAdapter2(
    private val listEpisode: List<EpisodeDetail2>,
    private val callGetMovieMedia: () -> Unit
) :
    RecyclerView.Adapter<EpisodeAdapter2.ViewHolder>() {

    class ViewHolder(binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val episode = binding.button

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode = listEpisode[position]
        holder.episode.text = "Tập: " + episode.seriesNo.toString()

        holder.episode.setOnClickListener {
            CompanionObject.episodeId = episode.episodeId
            MovieDetailActivity.positionEpisode = position

            callGetMovieMedia()
        }

    }

    override fun getItemCount(): Int = listEpisode.size
}
