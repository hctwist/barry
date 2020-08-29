package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.twisthenry8gmail.projectbarry.core.ForecastElement
import com.twisthenry8gmail.projectbarry.databinding.FeatureBoxBinding

class ForecastElementView(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val binding = FeatureBoxBinding.inflate(LayoutInflater.from(context), this, true)

    fun setElement(element: ForecastElement) {

        binding.element = element
        binding.executePendingBindings()
    }
}