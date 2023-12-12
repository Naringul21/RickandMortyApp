package com.example.rickmorty.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickmorty.databinding.RvItemsBinding
import com.example.rickmorty.network.model.Result
import com.example.rickmorty.utils.GenericDiffUtil
import kotlinx.coroutines.flow.Flow

class CharacterPagingAdapter() : PagingDataAdapter<Result, CharacterPagingAdapter.MyViewHolder>(
    GenericDiffUtil(
    myItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
    myContentsTheSame = { oldItem, newItem -> oldItem == newItem }
)) {


    inner class MyViewHolder(private val binding: RvItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Result) {
            binding.name.text = data.name
            binding.species.text = data.species
            Glide.with(binding.root).load(data.image).into(binding.homeImage)

            binding.root.setOnClickListener {
                onItemClickListener(data)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemsBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(getItem(position)!!)

        Log.e("list", "Success")

    }


    var onItemClickListener: ((Result) -> Unit) = {}



}