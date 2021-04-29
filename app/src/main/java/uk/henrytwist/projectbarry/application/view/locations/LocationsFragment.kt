package uk.henrytwist.projectbarry.application.view.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.showSoftKeyboard
import uk.henrytwist.projectbarry.databinding.LocationsFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    @Inject
    lateinit var viewModel: LocationsViewModel

    private lateinit var binding: LocationsFragmentBinding

    private lateinit var choiceAdapter: LocationChoiceAdapter
    private lateinit var searchAdapter: LocationSearchAdapter

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

        choiceAdapter = LocationChoiceAdapter(viewModel)
        searchAdapter = LocationSearchAdapter(viewModel)

        binding.locationsResults.layoutManager = LinearLayoutManager(context)

        binding.locationsInput.addTextChangedListener {

            viewModel.onSearchTextChanged(it.toString())
        }

        viewModel.searching.observe(viewLifecycleOwner) {

            binding.locationsResults.adapter = if (it) searchAdapter else choiceAdapter
        }

        viewModel.savedLocations.observe(viewLifecycleOwner) {

            if (it.isEmpty()) binding.locationsInput.showSoftKeyboard()
            choiceAdapter.submitList(it)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {

            searchAdapter.searchResults = it
            searchAdapter.notifyDataSetChanged()
        }
    }
}