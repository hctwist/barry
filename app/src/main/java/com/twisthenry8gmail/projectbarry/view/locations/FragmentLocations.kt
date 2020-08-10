package com.twisthenry8gmail.projectbarry.view.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.MarginItemDecoration
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.locations.LocationSearchResult
import com.twisthenry8gmail.projectbarry.databinding.FragmentLocationsBinding
import com.twisthenry8gmail.projectbarry.viewmodel.LocationsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

        viewModel.searching.observe(viewLifecycleOwner, Observer {

            binding.locationsResults.adapter = if (it) searchAdapter else pinnedAdapter
        })

        viewModel.locationChoices.observe(viewLifecycleOwner, Observer {

            pinnedAdapter.choices = it
            pinnedAdapter.notifyDataSetChanged()
        })

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {

            searchAdapter.places = it
            searchAdapter.notifyDataSetChanged()
        })

        setupResults()
    }

    private fun setupResults() {

        pinnedAdapter.clickListener = object : LocationChoiceAdapter.ClickListener {

            override fun onClick(choice: LocationChoiceAdapter.Choice) {

                viewModel.onClickChoice(choice)
            }

            override fun onPin(choice: LocationChoiceAdapter.Choice) {

                viewModel.onPin(choice)
            }
        }

        searchAdapter.clickListener = object : LocationSearchAdapter.ClickListener {

            override fun onClick(result: LocationSearchResult) {

                viewModel.onClickSearch(result)
            }
        }

        binding.locationsResults.run {

            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin)))
        }
    }
}