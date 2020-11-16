package com.twisthenry8gmail.projectbarry.application.view

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class StateFragmentContainerAdapter<T : Enum<T>>(private val manager: FragmentManager) {

    private var state: T? = null
    private var containerView: View? = null

    protected abstract fun getFragment(state: T): Fragment?

    fun attachTo(containerView: View) {

        if (containerView.id == View.NO_ID) {

            throw IllegalArgumentException("${this::class.simpleName} can only be attached to a container with an ID")
        }

        this.containerView = containerView
        updateState()
    }

    fun setState(newState: T) {

        if (state == newState) return

        state = newState
        updateState()
    }

    private fun updateState() {

        state?.also { state ->

            containerView?.also { containerView ->

                val tag = state.name

                val existingFragment = manager.findFragmentByTag(tag)
                val fragment = getFragment(state)

                if(fragment == null) {

                    existingFragment?.let { manager.beginTransaction().remove(it).commit() }
                }
                else if (existingFragment == null) {

                    manager.beginTransaction().replace(containerView.id, fragment, tag).commit()
                }
            }
        }
    }
}