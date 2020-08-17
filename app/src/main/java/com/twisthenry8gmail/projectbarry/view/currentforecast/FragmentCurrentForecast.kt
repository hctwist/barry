package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.twisthenry8gmail.projectbarry.databinding.FragmentCurrentForecastBinding
import com.twisthenry8gmail.projectbarry.view.FeatureAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentCurrentForecast : Fragment() {

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

        viewModel.features.observe(viewLifecycleOwner, Observer {

            featureAdapter.features = it
            featureAdapter.notifyDataSetChanged()
        })

        setupFeatureGrid()
    }

    private fun setupFeatureGrid() {

        binding.currentForecastFeatures.run {

            layoutManager = GridLayoutManager(context, 3)
            adapter = featureAdapter
        }
    }
}