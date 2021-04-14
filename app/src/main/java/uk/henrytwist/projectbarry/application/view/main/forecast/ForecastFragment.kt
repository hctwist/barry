package uk.henrytwist.projectbarry.application.view.main.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.main.MainViewModel
import uk.henrytwist.projectbarry.databinding.ForecastFragmentBinding

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ForecastFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    private val nowElementAdapter = ForecastElementAdapter()

    private val hourSnapshotAdapter = HourSnapshotAdapter()

    private lateinit var binding: ForecastFragmentBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = ForecastFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        viewModel.elements.observe(viewLifecycleOwner) {

            nowElementAdapter.elements = it
            nowElementAdapter.notifyDataSetChanged()
        }

        viewModel.hourSnapshots.observe(viewLifecycleOwner) {

            hourSnapshotAdapter.snapshots = it
            hourSnapshotAdapter.notifyDataSetChanged()
        }

        setupNowElements()
        setupHourSnapshots()
    }

    private fun setupNowElements() {

        binding.mainNowElements2.run {

            layoutManager = LinearLayoutManager(context)
            adapter = nowElementAdapter
        }
    }

    private fun setupHourSnapshots() {

        binding.mainHourlySnapshots.run {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourSnapshotAdapter
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin)))
        }
    }
}