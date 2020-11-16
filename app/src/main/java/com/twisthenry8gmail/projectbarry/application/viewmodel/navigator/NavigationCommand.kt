package com.twisthenry8gmail.projectbarry.application.viewmodel.navigator

import androidx.annotation.IdRes
import androidx.navigation.NavController

interface NavigationCommand {

    fun navigateWith(navController: NavController)

    class To(@IdRes private val id: Int) : NavigationCommand {

        override fun navigateWith(navController: NavController) {

            navController.navigate(id)
        }
    }

    object Back : NavigationCommand {

        override fun navigateWith(navController: NavController) {

            navController.popBackStack()
        }
    }
}