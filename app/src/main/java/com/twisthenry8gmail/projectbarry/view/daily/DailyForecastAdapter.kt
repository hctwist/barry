package com.twisthenry8gmail.projectbarry.view.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.data.DailyForecast
import com.twisthenry8gmail.projectbarry.databinding.DailyRowBinding

class DailyForecastAdapter: RecyclerView.Adapter<DailyForecastAdapter.VH>() {

    var forecasts = listOf<DailyForecast>()

    override fun getItemCount(): Int {

        return forecasts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(DailyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(forecasts[position])
    }

    class VH(private val binding: DailyRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: DailyForecast) {

            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }
}