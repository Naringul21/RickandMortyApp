package com.example.rickmorty.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import com.example.rickmorty.adapter.CharacterPagingAdapter
import com.example.rickmorty.databinding.FragmentHomeBinding
import com.example.rickmorty.presentation.viewmodel.HomeViewModel
import com.example.rickmorty.utils.BaseFragment
import com.example.rickmorty.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :BaseFragment<FragmentHomeBinding>(
    onInflate = FragmentHomeBinding::inflate
) {

    private val homeViewModel by viewModels<HomeViewModel>()

    private val characterAdapter by lazy {
        CharacterPagingAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = characterAdapter
        setViewModelObservers()
        setClickListener()

    }


    private fun setViewModelObservers() = lifecycleScope.launch {
        homeViewModel.getCharacters().flowWithLifecycle(lifecycle).collectLatest {
            characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            Log.d("TEST RESPONSE RESULT", "response $it")

        }
    }

    private fun setClickListener() {
        characterAdapter.onItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)

            )
        }
    }
}

