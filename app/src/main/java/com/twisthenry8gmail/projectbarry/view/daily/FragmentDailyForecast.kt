package com.twisthenry8gmail.projectbarry.view.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.databinding.FragmentDailyForecastBinding
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentDailyForecast : Fragment() {

    private val viewModel by viewModels<DailyForecastViewModel>(ownerProducer = { requireParentFragment() })

    private lateinit var binding: FragmentDailyForecastBinding

    private val forecastAdapter = DailyForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDailyForecastBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.successfulLocation.observe(viewLifecycleOwner) {

            forecastAdapter.location = it
            forecastAdapter.notifyItemChanged(0)
        }

        viewModel.forecast.observe(viewLifecycleOwner) {

            forecastAdapter.forecast = it
            forecastAdapter.notifyDataSetChanged()
        }

        setupForecastList()
    }

    private fun setupForecastList() {

        binding.dailyList.run {

            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
        }
    }
}