package com.twisthenry8gmail.projectbarry.view.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.view.MarginItemDecoration
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.databinding.FragmentDailyForecastBinding
import com.twisthenry8gmail.projectbarry.viewmodel.DailyForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDailyForecast : Fragment() {

    @Inject
    lateinit var viewModel: DailyForecastViewModel

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

        viewModel.successfulForecasts.observe(viewLifecycleOwner, Observer {

            forecastAdapter.forecasts = it
            forecastAdapter.notifyDataSetChanged()
        })

        setupForecastList()
    }

    private fun setupForecastList() {

        binding.dailyList.run {

            layoutManager = LinearLayoutManager(context)
            adapter = forecastAdapter
            addItemDecoration(
                MarginItemDecoration(
                    resources.getDimension(R.dimen.large_content_margin)
                )
            )
        }
    }
}