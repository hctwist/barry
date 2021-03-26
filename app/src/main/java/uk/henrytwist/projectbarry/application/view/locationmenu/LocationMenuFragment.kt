package uk.henrytwist.projectbarry.application.view.locationmenu

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.MenuFragment
import uk.henrytwist.projectbarry.domain.models.MenuLocation

@AndroidEntryPoint
class LocationMenuFragment : MenuFragment() {

    private val viewModel by viewModels<LocationMenuViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.locations.observe(viewLifecycleOwner) { locations ->

            navigationMenuView.buildMenu {

                locations.forEach {

                    addItem(resolveName(it), resolveIcon(it), it.selected) {

                        viewModel.onLocationClicked(it)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun resolveIcon(location: MenuLocation): Drawable? {

        return ContextCompat.getDrawable(requireContext(), when (location) {

            is MenuLocation.Current -> R.drawable.outline_gps_fixed_24
            is MenuLocation.Saved -> R.drawable.outline_pin_drop_24
        })
    }

    private fun resolveName(location: MenuLocation): String {

        return when (location) {

            is MenuLocation.Current -> requireContext().getString(R.string.current_location)
            is MenuLocation.Saved -> location.name
        }
    }
}