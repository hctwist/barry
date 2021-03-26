package uk.henrytwist.projectbarry.application.view.hourly

import uk.henrytwist.projectbarry.R
import uk.henrytwist.androidbasics.recyclerview.SingleBindingItemAdapter
import uk.henrytwist.projectbarry.databinding.HourlyHeaderBinding
import uk.henrytwist.projectbarry.domain.models.ConditionChange

class HourlyHeaderAdapter : SingleBindingItemAdapter<HourlyHeaderBinding>(R.layout.hourly_header) {

    lateinit var handler: Handler
    var change: ConditionChange? = null

    override fun onBind(binding: HourlyHeaderBinding) {

        binding.handler = handler
        binding.change = change
    }

    interface Handler {

        fun onTypeChanged(type: HourlyElementType)
    }
}