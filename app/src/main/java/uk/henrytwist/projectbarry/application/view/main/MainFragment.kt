package uk.henrytwist.projectbarry.application.view.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.androidbasics.livedata.observeEvent
import uk.henrytwist.androidbasics.recyclerview.MarginItemDecoration
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.MainFragmentBinding

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>()

    private val nowElementAdapter = ForecastElementAdapter()

    private val hourSnapshotAdapter = HourSnapshotAdapter()

    private lateinit var binding: MainFragmentBinding

    private val locationPermissionResultRegistration = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        viewModel.onLocationPermissionResult(it, true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.observeNavigation(this)

        viewModel.locationPermissionQuery.observeEvent(viewLifecycleOwner) {

            val permissionStatus = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            viewModel.onLocationPermissionResult(
                    permissionStatus == PackageManager.PERMISSION_GRANTED,
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            )
        }

        viewModel.requestLocationPermission.observeEvent(viewLifecycleOwner) {

            locationPermissionResultRegistration.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {

            binding.forecastContainerSwipeRefresh.isRefreshing = it
        }

        binding.forecastContainerSwipeRefresh.setOnRefreshListener {

            viewModel.onRefresh()
        }

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