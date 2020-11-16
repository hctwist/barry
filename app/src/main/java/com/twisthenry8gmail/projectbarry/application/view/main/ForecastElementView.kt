package com.twisthenry8gmail.projectbarry.application.view.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.twisthenry8gmail.projectbarry.databinding.ForecastElementBinding
import com.twisthenry8gmail.projectbarry.domain.core.ForecastElement

class ForecastElementView(context: Context, attrs: AttributeSet? = null) : FrameLayout(
    context,
    attrs
) {

    private val binding = ForecastElementBinding.inflate(LayoutInflater.from(context), this, true)

    fun setElement(element: ForecastElement?) {

        if (element == null) {


        } else {

            binding.element = element
            binding.executePendingBindings()
        }
    }
}