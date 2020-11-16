package com.twisthenry8gmail.projectbarry.application.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.twisthenry8gmail.projectbarry.databinding.FragmentLocationPermissionBinding
import com.twisthenry8gmail.projectbarry.application.view.PermissionHelper
import com.twisthenry8gmail.projectbarry.application.viewmodel.LocationPermissionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLocationPermission : Fragment() {

    private val viewModel by viewModels<LocationPermissionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLocationPermissionBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(viewLifecycleOwner) {

            it.navigateWith(findNavController())
        }

        viewModel.locationPermissionRequest.observe(viewLifecycleOwner) {

            PermissionHelper.requestLocationPermission(this)
        }
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
}