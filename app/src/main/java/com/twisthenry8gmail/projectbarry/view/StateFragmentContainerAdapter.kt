package com.twisthenry8gmail.projectbarry.view

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class StateFragmentContainerAdapter<T : Enum<T>>(private val manager: FragmentManager) {

    private var state: T? = null
    private var containerView: View? = null

    protected abstract fun getFragment(state: T): Fragment

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

        state?.let { state ->

            containerView?.let { containerView ->

                val tag = state.name

                val existingFragment = manager.findFragmentByTag(tag)

                if (existingFragment == null) {

                    val fragment = getFragment(state)
                    manager.beginTransaction().replace(containerView.id, fragment, tag)
                        .commit()
                }
            }
        }
    }
}