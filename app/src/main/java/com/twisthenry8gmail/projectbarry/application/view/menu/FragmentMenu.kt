package com.twisthenry8gmail.projectbarry.application.view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation
import com.twisthenry8gmail.projectbarry.application.viewmodel.MenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FragmentMenu : BottomSheetDialogFragment() {

    private val viewModel by viewModels<MenuViewModel>()

    private val menuAdapter = MenuAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupMenu()

        viewModel.observeNavigation(viewLifecycleOwner) {

            it.navigateWith(findNavController())
        }

        viewModel.locations.observe(viewLifecycleOwner) {

            menuAdapter.locations = it
            menuAdapter.notifyDataSetChanged()
        }
    }

    private fun setupMenu() {

        menuAdapter.clickHandler = object : MenuAdapter.ClickHandler {

            override fun onSettingsClicked() {

                viewModel.onSettingsClicked()
                dismiss()
            }

            override fun onLocationSettingsClicked() {

                viewModel.onLocationSettingsClicked()
                dismiss()
            }

            override fun onLocationClicked(location: MenuLocation) {

                viewModel.onLocationClicked(location)
                dismiss()
            }
        }

        menu_view.run {

            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }
    }
}