package uk.henrytwist.projectbarry.application.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.henrytwist.androidbasics.navigationmenuview.NavigationMenuView
import uk.henrytwist.projectbarry.R

abstract class MenuFragment : BottomSheetDialogFragment() {

    protected val navigationMenuView: NavigationMenuView
        get() = requireView() as NavigationMenuView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.generic_menu, container, false)
    }
}