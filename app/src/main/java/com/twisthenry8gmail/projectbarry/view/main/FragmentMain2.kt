package com.twisthenry8gmail.projectbarry.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.twisthenry8gmail.projectbarry.databinding.FragmentMain2Binding
import com.twisthenry8gmail.projectbarry.observeEvent
import com.twisthenry8gmail.projectbarry.view.PermissionHelper
import com.twisthenry8gmail.projectbarry.view.currentforecast.ForecastElementAdapter
import com.twisthenry8gmail.projectbarry.view.currentforecast.HourSnapshotAdapter
import com.twisthenry8gmail.projectbarry.view.menu.FragmentMenu
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentMain2 : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    private val nowElementAdapter = ForecastElementAdapter()
    private val hourSnapshotAdapter = HourSnapshotAdapter()
    private val daySnapshotAdapter = DaySnapshotAdapter()

    private lateinit var binding: FragmentMain2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMain2Binding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(viewLifecycleOwner) {

            it.navigateWith(findNavController())
        }

        viewModel.locationPermissionQuery.observe(viewLifecycleOwner) {

            val permissionStatus =
                requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            viewModel.onLocationPermissionResult(permissionStatus == PackageManager.PERMISSION_GRANTED)
        }

        viewModel.showMenu.observeEvent(viewLifecycleOwner) {

            showMenu()
        }

        viewModel.elements.observe(viewLifecycleOwner) {

            nowElementAdapter.elements = it
            nowElementAdapter.notifyDataSetChanged()
        }

        viewModel.hourSnapshots.observe(viewLifecycleOwner) {

            hourSnapshotAdapter.snapshots = it
            hourSnapshotAdapter.notifyDataSetChanged()
        }

        viewModel.daySnapshots.observe(viewLifecycleOwner) {

            daySnapshotAdapter.snapshots = it
            daySnapshotAdapter.notifyDataSetChanged()
        }

        setupNowElements()
        setupHourSnapshots()
        setupDaySnapshots()
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

    private fun showMenu() {

        FragmentMenu().show(childFragmentManager, null)
    }

    private fun setupNowElements() {

        binding.mainNow.mainNowElements.run {

            layoutManager = LinearLayoutManager(context)
            adapter = nowElementAdapter
        }
    }

    private fun setupHourSnapshots() {

        binding.mainHourly.mainHourlyItems.run {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourSnapshotAdapter
        }
    }

    private fun setupDaySnapshots() {

        binding.mainDaily.mainDailyItems.run {

            layoutManager = LinearLayoutManager(context)
            adapter = daySnapshotAdapter
        }
    }
}