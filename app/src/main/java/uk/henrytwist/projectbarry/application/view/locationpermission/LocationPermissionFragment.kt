package uk.henrytwist.projectbarry.application.view.locationpermission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import uk.henrytwist.projectbarry.databinding.FragmentLocationPermissionBinding
import uk.henrytwist.projectbarry.application.view.PermissionHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationPermissionFragment : Fragment() {

    private val viewModel by viewModels<LocationPermissionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentLocationPermissionBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

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