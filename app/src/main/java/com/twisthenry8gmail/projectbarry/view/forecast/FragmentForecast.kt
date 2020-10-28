package com.twisthenry8gmail.projectbarry.view.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.core.MainState
import com.twisthenry8gmail.projectbarry.databinding.FragmentForecastBinding
import com.twisthenry8gmail.projectbarry.view.PermissionHelper
import com.twisthenry8gmail.projectbarry.view.currentforecast.ForecastElementAdapter
import com.twisthenry8gmail.projectbarry.view.currentforecast.HourSnapshotAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentForecast : Fragment() {

    private val viewModel by viewModels<ForecastViewModel>()

    private lateinit var binding: FragmentForecastBinding

    private val elementAdapter = ForecastElementAdapter()
    private val hourSnapshotAdapter = HourSnapshotAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentForecastBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.state.observe(viewLifecycleOwner) {

            if (it == MainState.LOADING) {

                binding.currentForecastSkeleton.toggleSkeleton(true)
            } else {

                binding.currentForecastSkeleton.toggleSkeleton(false)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        viewModel.onLocationPermissionResult(
            PermissionHelper.isLocationPermissionGranted(
                requestCode,
                permissions,
                grantResults
            )
        )
    }

    private fun setupElementGrid() {

        binding.currentForecastElements.run {

            layoutManager = GridLayoutManager(context, 2)
            adapter = elementAdapter
        }
    }

    private fun setupHourSnapshots() {

        binding.currentForecastHourSnapshots.run {

            layoutManager = LinearLayoutManager(context)
            adapter = hourSnapshotAdapter
        }
    }
}