package uk.henrytwist.projectbarry.application.view.hourly

import android.util.Log
import uk.henrytwist.androidbasics.recyclerview.SingleBindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.HourlyHeaderBinding

class HourlyHeaderAdapter : SingleBindingItemAdapter<HourlyHeaderBinding>(R.layout.hourly_header) {

    lateinit var handler: Handler
    var selectedType: HourlyForecastType? = null

    override fun onBind(binding: HourlyHeaderBinding) {

        binding.selectedType = selectedType
        binding.handler = handler
    }

    interface Handler {

        fun onTypeChanged(type: HourlyForecastType)
    }
}