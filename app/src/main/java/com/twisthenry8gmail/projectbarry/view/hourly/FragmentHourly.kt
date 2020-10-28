package com.twisthenry8gmail.projectbarry.view.hourly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.databinding.FragmentHourlyBinding
import com.twisthenry8gmail.projectbarry.viewmodel.HourlyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentHourly : Fragment() {

    private val viewModel by viewModels<HourlyForecastViewModel>(ownerProducer = { requireParentFragment() })

    private lateinit var binding: FragmentHourlyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHourlyBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.hourlyForecast.observe(viewLifecycleOwner) {

            binding.hourlyGraph.setForecast(it)
        }

        binding.hourlyGraphToggles.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {

                viewModel.onForecastTypeSelected(
                    when (checkedId) {

                        R.id.hourly_graph_toggle_temp -> HourlyForecastViewModel.ForecastType.TEMPERATURE
                        R.id.hourly_graph_toggle_pop -> HourlyForecastViewModel.ForecastType.POP
                        else -> throw RuntimeException("Invalid view ID for toggle")
                    }
                )
            }
        }
    }
}