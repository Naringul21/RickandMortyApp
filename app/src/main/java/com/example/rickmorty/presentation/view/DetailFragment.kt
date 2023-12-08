package com.example.rickmorty.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.rickmorty.R
import com.example.rickmorty.databinding.FragmentDetailBinding
import com.example.rickmorty.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>
    (onInflate = FragmentDetailBinding ::inflate) {

        private val args:DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.setupViews()
        binding.detailBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun FragmentDetailBinding.setupViews() {
        val characters = args.characters
        detailName.text = characters.name
        detailGender.text = characters.gender
        detailOrigin.text = characters.origin?.name
        detailSpecies.text = characters.species
        detailStatus.text=characters.status
        detailType.text=characters.type
        Glide.with(root).load(characters.image).into(detailImg)
    }

}