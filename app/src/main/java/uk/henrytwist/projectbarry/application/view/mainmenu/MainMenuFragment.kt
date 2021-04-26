package uk.henrytwist.projectbarry.application.view.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.henrytwist.androidbasics.navigationmenuview.NavigationMenuView
import uk.henrytwist.projectbarry.R

class MainMenuFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.generic_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val menuView = view as NavigationMenuView

        menuView.buildMenu {

            addItem(R.string.locations_title, R.drawable.outline_place_24, false) {

                navigate(R.id.action_mainMenuFragment_to_fragmentLocations)
            }

            addItem(R.string.settings_title, R.drawable.outline_settings_24, false) {

                navigate(R.id.action_mainMenuFragment_to_settingsFragment)
            }
        }
    }

    private fun navigate(id: Int) {

        findNavController().navigate(id)
    }
}