package com.twisthenry8gmail.projectbarry.view

import androidx.fragment.app.viewModels
import com.twisthenry8gmail.projectbarry.view.errors.ErrorModel
import com.twisthenry8gmail.projectbarry.view.errors.FragmentError
import com.twisthenry8gmail.projectbarry.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentLocationError : FragmentError() {

    private val mainViewModel by viewModels<MainViewModel>(ownerProducer = {
        requireParentFragment().requireParentFragment().requireParentFragment()
    })

    override fun getErrorModel(): ErrorModel {

        return ErrorModel.locationErrorModel(requireContext()) {

            mainViewModel.fetchSelectedLocation()
        }
    }
}