package com.twisthenry8gmail.projectbarry.application.view.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.databinding.FragmentLocationsBinding
import com.twisthenry8gmail.projectbarry.application.view.MarginItemDecoration
import com.twisthenry8gmail.projectbarry.application.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentLocations : Fragment() {

    @Inject
    lateinit var viewModel: LocationsViewModel

    private lateinit var binding: FragmentLocationsBinding

    private val pinnedAdapter = LocationChoiceAdapter()
    private val searchAdapter = LocationSearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(viewLifecycleOwner) {

            it.navigateWith(findNavController())
        }

        binding.locationsInput.addTextChangedListener {

            viewModel.onSearchTextChanged(it.toString())
        }

        viewModel.searching.observe(viewLifecycleOwner) {

            binding.locationsResults.adapter = if (it) searchAdapter else pinnedAdapter
        }

        viewModel.savedLocations.observe(viewLifecycleOwner) {

            pinnedAdapter.rows = it
            pinnedAdapter.notifyDataSetChanged()
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {

            searchAdapter.places = it
            searchAdapter.notifyDataSetChanged()
        }

        setupResults()
    }

    private fun setupResults() {

        pinnedAdapter.clickHandler = object : LocationChoiceAdapter.ClickHandler {

            override fun onClick(location: SavedLocation) {

                viewModel.onClickChoice(location)
            }

            override fun onPin(location: SavedLocation) {

                viewModel.onPin(location)
            }
        }

        searchAdapter.clickListener = object : LocationSearchAdapter.ClickListener {

            override fun onClick(result: LocationSearchResult) {

                viewModel.onClickSearch(result)
            }
        }

        binding.locationsResults.run {

            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.margin)
                )
            )
        }
    }
}