package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentCurrentForecast : Fragment() {

    private val viewModel by viewModels<CurrentForecastViewModel>(ownerProducer = { requireParentFragment() })

//    private lateinit var binding: FragmentCurrentForecastBinding

    private val elementAdapter = ForecastElementAdapter()

    private val hourSnapshotAdapter = HourSnapshotAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
//        binding.viewmodel = viewModel
//        binding.lifecycleOwner = viewLifecycleOwner

//        return binding.root

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner) {

            if (it == MainState.LOADING) {

//                binding.currentForecastSkeleton.toggleSkeleton(true)
            } else {

//                binding.currentForecastSkeleton.toggleSkeleton(false)
            }
        }

        viewModel.elements.observe(viewLifecycleOwner) {

            elementAdapter.elements = it
            elementAdapter.notifyDataSetChanged()
        }

        viewModel.hourSnapshots.observe(viewLifecycleOwner) {

            hourSnapshotAdapter.snapshots = it
            hourSnapshotAdapter.notifyDataSetChanged()
        }

        setupElementGrid()
        setupHourSnapshots()
    }

    private fun setupElementGrid() {

//        binding.currentForecastElements.run {
//
//            layoutManager = GridLayoutManager(context, 2)
//            adapter = elementAdapter
//        }
    }

    private fun setupHourSnapshots() {

//        binding.currentForecastHourSnapshots.run {
//
//            layoutManager = LinearLayoutManager(context)
//            adapter = hourSnapshotAdapter
//        }
    }
}