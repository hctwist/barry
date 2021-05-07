package uk.henrytwist.projectbarry.application.view.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.main.forecast.ForecastFragment
import uk.henrytwist.projectbarry.application.view.main.loading.MainLoadingFragment
import uk.henrytwist.projectbarry.application.view.main.locationerror.LocationErrorFragment
import uk.henrytwist.projectbarry.application.view.main.networkerror.NetworkErrorFragment
import uk.henrytwist.projectbarry.application.view.main.permissionerror.LocationPermissionFragment
import uk.henrytwist.projectbarry.databinding.MainFragmentContainerBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragmentContainer : Fragment() {

    private lateinit var binding: MainFragmentContainerBinding

    private val viewModel by viewModels<MainViewModel>()

    private val locationPermissionResultRegistration = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        viewModel.onLocationPermissionResult(it, shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private val locationServicesResultRegistration = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

        viewModel.onLocationServicesResult(it.resultCode == Activity.RESULT_OK, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = MainFragmentContainerBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        viewModel.queryLocationPermission.observeEvent(viewLifecycleOwner) {

            val permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            viewModel.onLocationQueryResult(
                    permissionStatus == PackageManager.PERMISSION_GRANTED,
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            )
        }

        viewModel.requestLocationPermission.observeEvent(viewLifecycleOwner) {

            locationPermissionResultRegistration.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        viewModel.queryLocationServices.observeEvent(viewLifecycleOwner) {

            LocationSettingsUtil.isLocationEnabled(requireContext()) { enabled, exception ->

                viewModel.onLocationServicesResult(enabled, exception)
            }
        }

        viewModel.requestLocationServices.observeEvent(viewLifecycleOwner) {

            locationServicesResultRegistration.launch(IntentSenderRequest.Builder(it.resolution).build())
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {

            binding.forecastContainerSwipeRefresh.isRefreshing = it
        }

        binding.forecastContainerSwipeRefresh.setOnRefreshListener {

            viewModel.onRefresh()
        }

        viewModel.status.observe(viewLifecycleOwner) { status ->

            requireNotNull(status)

            // TODO Try and find a way to remove the need for this
            childFragmentManager.executePendingTransactions()

            val transaction = childFragmentManager.beginTransaction()
            var skipAdding = false

            childFragmentManager.fragments.forEach {

                val fragmentStatus = MainViewModel.Status.valueOf(it.tag!!)

                if (fragmentStatus != status || skipAdding) {

                    transaction.remove(it)
                } else {

                    skipAdding = true
                }
            }

            if (!skipAdding) {

                val fragmentToAdd = when (status) {

                    MainViewModel.Status.LOADED -> ForecastFragment()
                    MainViewModel.Status.LOADING -> MainLoadingFragment()
                    MainViewModel.Status.NO_PERMISSION -> LocationPermissionFragment()
                    MainViewModel.Status.NETWORK_ERROR -> NetworkErrorFragment()
                    MainViewModel.Status.LOCATION_ERROR -> LocationErrorFragment()
                }

                val container = if (status == MainViewModel.Status.LOADED) R.id.main_container else R.id.main_error_container
                transaction.add(container, fragmentToAdd, status.name)
            }

            transaction.commit()
        }
    }

    override fun onResume() {

        viewModel.onResume()
        super.onResume()
    }
}