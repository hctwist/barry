package uk.henrytwist.projectbarry.application.view.main

import uk.henrytwist.androidbasics.recyclerview.BindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.ForecastElementBinding
import uk.henrytwist.projectbarry.domain.models.ForecastElement

class ForecastElementAdapter : BindingItemAdapter<ForecastElementBinding>(R.layout.forecast_element) {

    var elements = listOf<ForecastElement>()

    override fun getItemCount(): Int {

        return elements.size
    }

    override fun onBind(binding: ForecastElementBinding, position: Int) {

        binding.element = elements[position]
    }
}