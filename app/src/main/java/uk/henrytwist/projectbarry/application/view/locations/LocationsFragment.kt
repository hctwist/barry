package uk.henrytwist.projectbarry.application.view.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration
import uk.henrytwist.androidbasics.showSoftKeyboard
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.LocationsFragmentBinding
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult
import uk.henrytwist.projectbarry.domain.models.SavedLocation
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    @Inject
    lateinit var viewModel: LocationsViewModel

    private lateinit var binding: LocationsFragmentBinding

    private val pinnedAdapter = LocationChoiceAdapter()
    private val searchAdapter = LocationSearchAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = LocationsFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        binding.locationsInput.addTextChangedListener {

            viewModel.onSearchTextChanged(it.toString())
        }

        viewModel.searching.observe(viewLifecycleOwner) {

            binding.locationsResults.adapter = if (it) searchAdapter else pinnedAdapter
        }

        viewModel.savedLocations.observe(viewLifecycleOwner) {

            if (it.isEmpty()) binding.locationsInput.showSoftKeyboard()
            pinnedAdapter.submitList(it)
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