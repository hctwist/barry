package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.core.ForecastElement

class ForecastElementAdapter : RecyclerView.Adapter<ForecastElementAdapter.VH>() {

    var elements = listOf<ForecastElement>()

    override fun getItemCount(): Int {

        return elements.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(ForecastElementView(parent.context))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.forecastElementView.setElement(elements[position])
    }

    class VH(val forecastElementView: ForecastElementView) : RecyclerView.ViewHolder(forecastElementView)
}