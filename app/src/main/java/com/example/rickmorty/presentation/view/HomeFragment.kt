package com.example.rickmorty.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.DifferCallback
import androidx.recyclerview.widget.DiffUtil
import com.example.rickmorty.R
import com.example.rickmorty.adapter.CharacterPagingAdapter
import com.example.rickmorty.databinding.FragmentHomeBinding
import com.example.rickmorty.network.model.Result
import com.example.rickmorty.presentation.viewmodel.HomeViewModel
import com.example.rickmorty.utils.BaseFragment
import com.example.rickmorty.utils.Resource
import com.google.gson.internal.bind.ArrayTypeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(
    onInflate = FragmentHomeBinding::inflate
) {

    private val homeViewModel by viewModels<HomeViewModel>()

    private val characterAdapter by lazy {
        CharacterPagingAdapter()
    }

    private var query: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = characterAdapter
        setViewModelObservers()
        setClickListener()
        homeViewModel.getCharacters()

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, gender)
        binding.autoCompleteTextViewGender.setAdapter(arrayAdapter)

        val status = resources.getStringArray(R.array.status)
        val arrayAdapterStatus = ArrayAdapter(requireActivity(), R.layout.dropdown_item, status)
        binding.autoCompleteTextViewStatus.setAdapter(arrayAdapterStatus)

        getNameSearchView()

    }


    private fun setViewModelObservers() = lifecycleScope.launch {
        lifecycleScope.launch {
            homeViewModel.characterPagingData
                .flowWithLifecycle(lifecycle).
                collectLatest {
                    characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
        }

    }

    private fun setClickListener() {
        characterAdapter.onItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)

            )
        }
    }

    private fun getNameSearchView() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                homeViewModel.getFilteredCharacters(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                return false
            }

        })
    }


}

