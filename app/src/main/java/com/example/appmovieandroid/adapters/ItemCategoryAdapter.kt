package com.example.appmovieandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmovieandroid.databinding.ItemCategoryBinding
import com.example.appmovieandroid.models.MovieCategory

class ItemCategoryAdapter(
    private val listMovieCategory: List<MovieCategory>,
    private val context: Context,
    private val uid : String

) : RecyclerView.Adapter<ItemCategoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val recyclerviewCategory = binding.itemCategory
        val titleCategory = binding.titleCategory

    }

    override fun onCreateViewHolder(
        parent: ViewGroup, 
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemMovieAdapter = listMovieCategory[position]

        holder.titleCategory.text = itemMovieAdapter.titleCategory
        holder.recyclerviewCategory.setHasFixedSize(true)
        holder.recyclerviewCategory.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
        holder.recyclerviewCategory.adapter = ItemMovieAdapter(itemMovieAdapter.listMovie, context,uid)
    }

    override fun getItemCount(): Int = listMovieCategory.size
}