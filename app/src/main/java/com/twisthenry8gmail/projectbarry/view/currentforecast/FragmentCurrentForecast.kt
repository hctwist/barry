package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.twisthenry8gmail.projectbarry.MainState
import com.twisthenry8gmail.projectbarry.databinding.FragmentCurrentForecastBinding
import com.twisthenry8gmail.projectbarry.view.FeatureAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentCurrentForecast : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment().requireParentFragment() })
    private val viewModel by viewModels<CurrentForecastViewModel>(ownerProducer = { requireParentFragment() })

    private lateinit var binding: FragmentCurrentForecastBinding

    private val featureAdapter = FeatureAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner) {

            if (it == MainState.LOADING) {

                binding.currentForecastSkeleton.toggleSkeleton(true)
            } else {

                binding.currentForecastSkeleton.toggleSkeleton(false)
            }
        }

        viewModel.elements.observe(viewLifecycleOwner, {

            featureAdapter.elements = it
            featureAdapter.notifyDataSetChanged()
        })

        setupFeatureGrid()
    }

    private fun setupFeatureGrid() {

        binding.currentForecastFeatures.run {

            layoutManager = GridLayoutManager(context, 2)
            adapter = featureAdapter
        }
    }
}