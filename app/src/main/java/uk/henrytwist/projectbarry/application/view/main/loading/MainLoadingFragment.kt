package uk.henrytwist.projectbarry.application.view.main.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uk.henrytwist.projectbarry.application.view.main.MainViewModel
import uk.henrytwist.projectbarry.databinding.MainLoadingFragmentBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainLoadingFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel>(ownerProducer = { requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = MainLoadingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}