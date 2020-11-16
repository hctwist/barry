package com.twisthenry8gmail.projectbarry.application.view.main

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
import com.twisthenry8gmail.projectbarry.application.view.PermissionHelper
import com.twisthenry8gmail.projectbarry.application.view.menu.FragmentMenu
import com.twisthenry8gmail.projectbarry.application.viewmodel.MainViewModel
import com.twisthenry8gmail.projectbarry.databinding.FragmentMainBinding
import com.twisthenry8gmail.projectbarry.domain.core.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentMain : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    private val nowElementAdapter = ForecastElementAdapter()
    private val nowElementAdapter2 = ForecastElementAdapter2()
    private val nowElementAdapter3 = ForecastElementAdapter3()

    private val hourSnapshotAdapter = HourSnapshotAdapter()
    private val hourSnapshotAdapter2 = HourSnapshotAdapter2()

    private val daySnapshotAdapter = DaySnapshotAdapter()

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)
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

            nowElementAdapter2.elements = it
            nowElementAdapter2.notifyDataSetChanged()

            nowElementAdapter3.elements = it
            nowElementAdapter3.invalidate()
        }

        viewModel.hourSnapshots.observe(viewLifecycleOwner) {

            hourSnapshotAdapter.snapshots = it
            hourSnapshotAdapter.notifyDataSetChanged()

            hourSnapshotAdapter2.snapshots = it
            hourSnapshotAdapter2.invalidate()
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

//        binding.mainNow.mainNowElements.run {
//
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = nowElementAdapter
//
//            adapter = nowElementAdapter2
//        }

        binding.mainNow.mainNowElements.run {

            setAdapter(nowElementAdapter3)
        }
    }

    private fun setupHourSnapshots() {

        binding.mainHourly.mainHourlyItems.run {

//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//            adapter = hourSnapshotAdapter

            setAdapter(hourSnapshotAdapter2)
        }
    }

    private fun setupDaySnapshots() {

        binding.mainDaily.mainDailyItems.run {

            layoutManager = LinearLayoutManager(context)
            adapter = daySnapshotAdapter
        }
    }
}