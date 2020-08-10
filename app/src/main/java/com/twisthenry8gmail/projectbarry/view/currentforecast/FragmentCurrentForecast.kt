package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.twisthenry8gmail.projectbarry.*
import com.twisthenry8gmail.projectbarry.data.test.RoomModel
import com.twisthenry8gmail.projectbarry.databinding.FragmentCurrentForecastBinding
import com.twisthenry8gmail.projectbarry.view.HourlyAdapter
import com.twisthenry8gmail.projectbarry.viewmodel.CurrentForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCurrentForecast : Fragment() {

    @Inject
    lateinit var viewModel: CurrentForecastViewModel

    private lateinit var binding: FragmentCurrentForecastBinding

    private val featureAdapter = FeatureAdapter()

    private val hourlyAdapter = HourlyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestPerm()

        viewModel.features.observe(viewLifecycleOwner, Observer {

            featureAdapter.features = it
            featureAdapter.notifyDataSetChanged()
        })

        viewModel.showLocationMenu.observe(viewLifecycleOwner, Event.Observer {

            findNavController().navigate(R.id.action_fragmentMain_to_fragmentLocations)
        })

        viewModel.hourly.observe(viewLifecycleOwner, Observer {

            hourlyAdapter.hours = it
            hourlyAdapter.notifyDataSetChanged()
        })

        viewModel.refreshing.observe(viewLifecycleOwner, Observer {

            if (it) {

                binding.mainRefreshingLight.animate().rotationBy(180F)
                    .setInterpolator(LinearInterpolator()).setDuration(1000)
                    .withEndAction(object : Runnable {

                        override fun run() {

                            binding.mainRefreshingLight.animate().rotationBy(180F).setDuration(1000)
                                .setInterpolator(LinearInterpolator()).withEndAction(this)
                        }
                    })
            }
        })

        setupSwipeRefresh()
        setupFeatureGrid()
        setupHourly()
    }

    private fun setupSwipeRefresh() {

        binding.mainSwipeRefresh.onRefreshListener = {

            viewModel.forceRefresh()
        }
    }

    private fun setupFeatureGrid() {

        binding.mainFeatures.run {

            layoutManager = GridLayoutManager(context, 2)
            adapter = featureAdapter
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin)))
        }
    }

    private fun setupHourly() {

        binding.mainHourly.run {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
            addItemDecoration(MarginItemDecoration(resources.getDimension(R.dimen.margin)))
        }
    }

    // TODO Move and sort
    fun requestPerm() {

        val check = requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        when (check) {

            PackageManager.PERMISSION_GRANTED -> {


            }

            PackageManager.PERMISSION_DENIED -> {

                requestLocationPermission()
            }
        }
    }
}