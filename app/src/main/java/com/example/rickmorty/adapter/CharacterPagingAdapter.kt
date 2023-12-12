package com.example.rickmorty.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickmorty.R
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
            binding.homeImage.transitionName = data.image
            binding.root.setOnClickListener {
                onItemClickListener(data, binding.homeImage)
            }


            when (data.gender) {
                "Female" -> {
                    binding.icImg.setImageResource(R.drawable.ic_female)

                }

                "Male" -> {
                    binding.icImg.setImageResource(R.drawable.ic_male)
                }

                "Genderless" -> {
                    binding.icImg.setImageResource(R.drawable.ic_genderless)
                }

                "unknown" -> {
                    binding.icImg.setImageResource(R.drawable.ic_unknown)
                }
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


    lateinit var onItemClickListener: (Result, ImageView) -> Unit



}