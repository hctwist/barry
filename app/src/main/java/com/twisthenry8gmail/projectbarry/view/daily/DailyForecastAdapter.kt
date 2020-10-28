package com.twisthenry8gmail.projectbarry.view.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.core.DailyForecast
import com.twisthenry8gmail.projectbarry.core.ForecastLocation
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.databinding.DailyLocationRowBinding
import com.twisthenry8gmail.projectbarry.databinding.DailyRowBinding

class DailyForecastAdapter : RecyclerView.Adapter<DailyViewHolder>() {

    var location: SelectedLocation? = null
    var forecast = listOf<DailyForecast>()

    override fun getItemCount(): Int {

        return forecast.size + 1
    }

    override fun getItemViewType(position: Int): Int {

        return if (position == 0) VIEW_TYPE_LOCATION else VIEW_TYPE_DAY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_LOCATION) {

            DailyViewHolder.Location(DailyLocationRowBinding.inflate(inflater, parent, false))
        } else {

            DailyViewHolder.Forecast(DailyRowBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {

        if (holder is DailyViewHolder.Location) {

            location?.let { holder.bind(it) }
        } else if (holder is DailyViewHolder.Forecast) {

            holder.bind(forecast[position - 1])
        }
    }

    companion object {

        const val VIEW_TYPE_LOCATION = 0
        const val VIEW_TYPE_DAY = 1
    }
}