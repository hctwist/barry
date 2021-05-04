package uk.henrytwist.projectbarry.application.view.hourly

import android.util.Log
import uk.henrytwist.androidbasics.recyclerview.SingleBindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.HourlyHeaderBinding

class HourlyHeaderAdapter(private val handler: Handler) : SingleBindingItemAdapter<HourlyHeaderBinding>(R.layout.hourly_header) {

    var selectedType: HourlyForecastType? = null

    override fun onBind(binding: HourlyHeaderBinding) {

        binding.selectedType = selectedType
        binding.handler = handler
    }

    interface Handler {

        fun onTypeChanged(type: HourlyForecastType)
    }
}