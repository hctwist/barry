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
import com.twisthenry8gmail.projectbarry.Event
import com.twisthenry8gmail.projectbarry.databinding.FragmentMainBinding
import com.twisthenry8gmail.projectbarry.view.PermissionHelper
import com.twisthenry8gmail.projectbarry.view.menu.FragmentMenu
import com.twisthenry8gmail.projectbarry.view.navigationview.ShiftingNavigationView
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FragmentMain : Fragment() {

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

        viewModel.observeNavigation(viewLifecycleOwner) {

            it.navigateWith(findNavController())
        }

        viewModel.locationPermissionQuery.observe(viewLifecycleOwner, Event.Observer {

            val permissionStatus =
                requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            viewModel.onLocationPermissionResult(permissionStatus == PackageManager.PERMISSION_GRANTED)
        })

        viewModel.showMenu.observe(viewLifecycleOwner, Event.Observer {

            showMenu()
        })

        setupStateAdapter()
        setupNavigation()
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

    private fun setupNavigation() {

        binding.mainNavigation.itemSelectListener = ShiftingNavigationView.ItemSelectListener {

//            viewModel.onNavigationItemSelected(it)
        }
    }

    private fun setupStateAdapter() {

        stateAdapter.attachTo(binding.mainContainer)
    }
}