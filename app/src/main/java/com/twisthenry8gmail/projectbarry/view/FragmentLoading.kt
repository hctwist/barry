package com.twisthenry8gmail.projectbarry.view

import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.R
import kotlinx.android.synthetic.main.fragment_loading.*

abstract class FragmentLoading : Fragment(R.layout.fragment_loading) {

    fun setMessage(message: String) {

        loading_message.text = message
    }
}