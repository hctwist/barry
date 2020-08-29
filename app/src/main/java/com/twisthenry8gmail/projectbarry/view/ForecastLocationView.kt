package com.twisthenry8gmail.projectbarry.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.databinding.ForecastLocationViewBinding

class ForecastLocationView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding =
        ForecastLocationViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setLocation(location: ForecastLocation?) {

        binding.location = location
        binding.executePendingBindings()
    }
}