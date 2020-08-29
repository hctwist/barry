package com.twisthenry8gmail.projectbarry.viewmodel.navigator

import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.twisthenry8gmail.projectbarry.core.Event

abstract class NavigatorViewModel : ViewModel() {

    private val _navigationCommander = MutableLiveData<Event<NavigationCommand>>()

    fun navigate(navigationCommand: NavigationCommand) {

        _navigationCommander.value = Event(navigationCommand)
    }

    fun observeNavigation(lifecycleOwner: LifecycleOwner, observer: (NavigationCommand) -> Unit) {

        _navigationCommander.observe(lifecycleOwner, Event.Observer {

            observer(it)
        })
    }

    fun navigateTo(@IdRes id: Int)  = navigate(NavigationCommand.To(id))

    fun navigateBack() = navigate(NavigationCommand.Back)
}