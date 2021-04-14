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

    private lateinit var binding: LocationErrorFragmentBinding

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = LocationErrorFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}