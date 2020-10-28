package com.twisthenry8gmail.projectbarry.view.daily

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.core.DailyForecast
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.databinding.DailyLocationRowBinding
import com.twisthenry8gmail.projectbarry.databinding.DailyRowBinding

sealed class DailyViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    class Location(private val binding: DailyLocationRowBinding) : DailyViewHolder(binding) {

        fun bind(location: SelectedLocation) {

            binding.location = location
            binding.executePendingBindings()
        }
    }

    class Forecast(private val binding: DailyRowBinding) : DailyViewHolder(binding) {

        fun bind(forecast: DailyForecast) {

            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }
}