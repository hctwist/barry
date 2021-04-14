package uk.henrytwist.projectbarry.application.view.main.permissionerror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.application.view.main.MainViewModel
import uk.henrytwist.projectbarry.databinding.LocationPermissionFragmentBinding

@AndroidEntryPoint
class LocationPermissionFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val binding = LocationPermissionFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        return binding.root
    }
}