package uk.henrytwist.projectbarry.application.view.daily

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

@AndroidEntryPoint
class DailyFragment : Fragment(R.layout.daily_fragment) {

    private val viewModel by viewModels<DailyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        val headerAdapter = HeaderAdapter(viewModel, getString(R.string.daily_title))
        val dailyAdapter = DailyAdapter(viewModel)

        view.findViewById<RecyclerView>(R.id.daily_recycler).run {

            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(headerAdapter, dailyAdapter)
            itemAnimator = DailyExpansionAnimator()
        }

        viewModel.forecast.observe(viewLifecycleOwner) {

            dailyAdapter.submitList(it)
        }
    }
}