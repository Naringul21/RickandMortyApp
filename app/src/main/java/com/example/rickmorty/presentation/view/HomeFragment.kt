package com.example.rickmorty.presentation.view

import android.os.Bundle
import android.transition.TransitionInflater
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
import androidx.navigation.fragment.FragmentNavigatorExtras
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
    private var status_type = ""
    private var gender_type = ""




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = characterAdapter
        setViewModelObservers()
        setClickListener()
        homeViewModel.getCharacters()

        filterData()

        getNameSearchView()

        val animation =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation



    }


    private fun setViewModelObservers() = lifecycleScope.launch {
        lifecycleScope.launch {
            homeViewModel.characterPagingData
                .flowWithLifecycle(lifecycle).collectLatest {
                    characterAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
        }

    }

    private fun setClickListener() {
        characterAdapter.onItemClickListener = {data, img->

            val extras= FragmentNavigatorExtras(
                img to data.image!!
            )
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(data), extras

            )
            }
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
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

    override fun onResume() {
        super.onResume()

        val gender = resources.getStringArray(R.array.gender)
        binding.autoCompleteTextViewGender.setAdapter(null)
        val genderAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, gender)
        binding.autoCompleteTextViewGender.setAdapter(genderAdapter)


        val status = resources.getStringArray(R.array.status)
        binding.autoCompleteTextViewStatus.setAdapter(null)
        val statusAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, status)
        binding.autoCompleteTextViewStatus.setAdapter(statusAdapter)
    }

    private fun filterData() {
        val genders = resources.getStringArray(R.array.gender)
        val genderAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, genders)

        binding.autoCompleteTextViewGender.setOnItemClickListener { adapterView, view, position, l ->
            gender_type = genders[position]
            if (gender_type == "all") {
                gender_type = ""
                homeViewModel.getFilteredCharacters(
                    name = binding.searchView.toString(),
                    gender_type,
                    status_type
                )
            } else {
                homeViewModel.getFilteredCharacters(
                    name = binding.searchView.toString(),
                    gender_type,
                    status_type
                )
            }

        }

        binding.autoCompleteTextViewGender.setAdapter(genderAdapter)


        val status = resources.getStringArray(R.array.status)
        val statusAdapter = ArrayAdapter(requireActivity(), R.layout.dropdown_item, status)
        binding.autoCompleteTextViewStatus.setOnItemClickListener { adapterView, view, position, l ->
            status_type = status[position]
            if (status_type == "all") {
                status_type = ""
                homeViewModel.getFilteredCharacters(
                    name = binding.searchView.toString(),
                    gender_type,
                    status_type
                )
            } else {
                homeViewModel.getFilteredCharacters(
                    name = binding.searchView.toString(),
                    gender_type,
                    status_type
                )
            }}

        binding.autoCompleteTextViewStatus.setAdapter(statusAdapter)

    }
}

