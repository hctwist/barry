package uk.henrytwist.projectbarry.application.view.hourly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter
import uk.henrytwist.projectbarry.domain.models.HourlyForecast

@AndroidEntryPoint
class HourlyFragment : Fragment(R.layout.hourly_fragment) {

    private val viewModel by viewModels<HourlyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        val headerAdapter = HeaderAdapter(viewModel)
        val hourlyHeaderAdapter = HourlyHeaderAdapter(viewModel)

        val conditionAdapter = HourlyConditionAdapter()
        val elementAdapter = HourlyElementAdapter()

        val forecastAdapter = ConcatAdapter(headerAdapter, hourlyHeaderAdapter, conditionAdapter)

        viewModel.forecastType.observe(viewLifecycleOwner) {

            headerAdapter.title = getString(it.nameRes)
            headerAdapter.notifyDataSetChanged()

            hourlyHeaderAdapter.selectedType = it
            hourlyHeaderAdapter.notifyDataSetChanged()
        }

        viewModel.forecast.observe(viewLifecycleOwner) {

            when (it) {

                is HourlyForecast.Conditions -> {

                    if (forecastAdapter.removeAdapter(elementAdapter)) forecastAdapter.addAdapter(2, conditionAdapter)
                    conditionAdapter.rows = it.blocks
                    conditionAdapter.notifyDataSetChanged()
                }
                is HourlyForecast.Elements -> {

                    if (forecastAdapter.removeAdapter(conditionAdapter)) forecastAdapter.addAdapter(2, elementAdapter)
                    elementAdapter.rows = it.elements
                    elementAdapter.notifyDataSetChanged()
                }
            }
        }

        val recycler = view.findViewById<RecyclerView>(R.id.hourly_recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = forecastAdapter
    }
}