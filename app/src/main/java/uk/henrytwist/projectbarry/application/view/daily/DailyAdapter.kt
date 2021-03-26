package uk.henrytwist.projectbarry.application.view.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.DailyRowBinding
import uk.henrytwist.projectbarry.domain.models.DailyForecast

class DailyAdapter : RecyclerView.Adapter<DailyAdapter.Holder>() {

    var days = listOf<DailyForecast.Day>()

    override fun getItemCount(): Int {

        return days.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(DailyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(days[position])
    }

    class Holder(private val binding: DailyRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: DailyForecast.Day) {

            binding.day = day
            binding.executePendingBindings()
        }
    }
}