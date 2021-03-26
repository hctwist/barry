package uk.henrytwist.projectbarry.application.view.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.HourRowBinding
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.HourlyForecast

class HourlyAdapter : RecyclerView.Adapter<HourlyAdapter.Holder>() {

    var forecast: HourlyForecast<out ForecastElement>? = null

    override fun getItemCount(): Int {

        return forecast?.hours?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(HourRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        forecast?.let {

            holder.bind(it.minElementValue, it.maxElementValue, it.hours[position])
        }
    }

    class Holder(private val binding: HourRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(minElementValue: Double, maxElementValue: Double, hourly: HourlyForecast.Hour<*>) {

            binding.minElementValue = minElementValue
            binding.maxElementValue = maxElementValue
            binding.hour = hourly
            binding.executePendingBindings()
        }
    }
}