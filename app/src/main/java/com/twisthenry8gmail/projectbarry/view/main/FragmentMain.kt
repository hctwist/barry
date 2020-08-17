package com.twisthenry8gmail.projectbarry.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.twisthenry8gmail.projectbarry.Event
import com.twisthenry8gmail.projectbarry.databinding.FragmentMainBinding
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentMain : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 0

    private val viewModel by viewModels<MainViewModel>()

    private val stateAdapter by lazy { MainStateAdapter(childFragmentManager) }

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

        viewModel.locationPermissionRequest.observe(viewLifecycleOwner, Event.Observer {

            requestLocationPermission()
        })

        viewModel.state.observe(viewLifecycleOwner, Observer {

            stateAdapter.setState(it)
        })

        setupStateAdapter()
        setupNavigation()
        setupSwipeRefresh()
    }

    private fun requestLocationPermission() {

        // TODO View model checks selected location, requests permission check through livedata, still waiting for the result in the background. If it comes back granted, load on, otherwise show LocationPermissionDeniedFragment which says to change location or grant permission again
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            val state = if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MainViewModel.PermissionState.GRANTED
            } else MainViewModel.PermissionState.DENIED

            viewModel.registerPermissionState(state)
        }
    }

    private fun setupNavigation() {

        binding.mainNavigation.onItemSelected = {

            viewModel.onNavigationItemSelected(it)
        }
    }

    private fun setupStateAdapter() {

        stateAdapter.attachTo(binding.mainContainer)
    }

    private fun setupSwipeRefresh() {

        binding.mainSwipeRefresh.onRefreshListener = {

            viewModel.onSwipeRefresh()
        }
    }
}