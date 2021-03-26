package uk.henrytwist.projectbarry.application.view.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class DailyFragment : Fragment(R.layout.daily_fragment) {

    private val viewModel by viewModels<DailyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        val headerAdapter = HeaderAdapter().apply {

            handler = viewModel
        }
        val dailyAdapter = DailyAdapter()

        view.findViewById<RecyclerView>(R.id.daily_recycler).run {

            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(headerAdapter, dailyAdapter)
        }

        viewModel.forecast.observe(viewLifecycleOwner) {

            dailyAdapter.days = it.days
            dailyAdapter.notifyDataSetChanged()
        }
    }
}