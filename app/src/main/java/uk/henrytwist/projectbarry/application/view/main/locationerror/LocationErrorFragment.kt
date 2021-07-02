package uk.henrytwist.projectbarry.application.view.main.locationerror

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import uk.henrytwist.projectbarry.application.view.main.MainViewModel
import uk.henrytwist.projectbarry.databinding.LocationErrorFragmentBinding

class LocationErrorFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    private var _binding: LocationErrorFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = LocationErrorFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel

        return binding.root
    }

    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }
}