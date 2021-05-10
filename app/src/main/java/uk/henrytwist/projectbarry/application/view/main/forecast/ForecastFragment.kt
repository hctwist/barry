package uk.henrytwist.projectbarry.application.view.main.forecast

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.main.MainViewModel
import uk.henrytwist.projectbarry.databinding.ForecastFragmentBinding

@AndroidEntryPoint
class ForecastFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    private val nowElementAdapter = ForecastElementAdapter()

    private val hourSnapshotAdapter = HourSnapshotAdapter()

    private var _binding: ForecastFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = ForecastFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val landscapeConstraints = ConstraintSet()
            landscapeConstraints.clone(context, R.layout.forecast_fragment_landscape_blueprint)
            landscapeConstraints.applyTo(binding.root as ConstraintLayout)
        }

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