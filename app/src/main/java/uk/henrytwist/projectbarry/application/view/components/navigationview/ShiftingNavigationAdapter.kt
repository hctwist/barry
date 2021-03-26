package uk.henrytwist.projectbarry.application.view.components.navigationview

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class ShiftingNavigationAdapter(private val containerViewId: Int, private val manager: FragmentManager) {

    abstract fun getFragment(id: Int): Fragment

    internal fun onItemSelected(id: Int) {

        manager.beginTransaction().replace(containerViewId, getFragment(id)).commit()
    }
}