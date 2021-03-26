package uk.henrytwist.projectbarry.application.view.hourly

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HourlyFragment : Fragment(R.layout.hourly_fragment) {

    private val viewModel by viewModels<HourlyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val headerAdapter = HeaderAdapter().apply {

            handler = viewModel
        }
        val hourlyHeaderAdapter = HourlyHeaderAdapter().apply {

            handler = viewModel
        }
        val hourlyAdapter = HourlyAdapter()

        view.findViewById<RecyclerView>(R.id.hourly_recycler).run {

            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(headerAdapter, hourlyHeaderAdapter, hourlyAdapter)
        }

        viewModel.forecast.observe(viewLifecycleOwner) {

            hourlyHeaderAdapter.change = it.change
            hourlyHeaderAdapter.notifyDataSetChanged()

            hourlyAdapter.forecast = it
            hourlyAdapter.notifyDataSetChanged()
        }
    }
}