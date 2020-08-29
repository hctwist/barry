package com.twisthenry8gmail.projectbarry.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.core.HourlyForecast
import com.twisthenry8gmail.projectbarry.databinding.HourlyRowBinding

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.VH>() {

    var hours = listOf<HourlyForecast>()

    override fun getItemCount(): Int {

        return hours.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(HourlyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(hours[position])
    }

    class VH(val binding: HourlyRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(forecast: HourlyForecast) {

            binding.forecast = forecast
            binding.executePendingBindings()
        }
    }
}