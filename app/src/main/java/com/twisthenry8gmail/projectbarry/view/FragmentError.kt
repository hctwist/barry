package com.twisthenry8gmail.projectbarry.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.twisthenry8gmail.projectbarry.R
import kotlinx.android.synthetic.main.fragment_error.*

abstract class FragmentError : Fragment(R.layout.fragment_error) {

    abstract fun getErrorImageRes(): Int

    abstract fun getErrorMessage(): String

    abstract fun getErrorButtonText(): String

    abstract fun onErrorButtonClicked()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        error_image.setImageResource(getErrorImageRes())
        error_message.text = getErrorMessage()
        error_button.text = getErrorButtonText()
        error_button.setOnClickListener { onErrorButtonClicked() }
    }
}