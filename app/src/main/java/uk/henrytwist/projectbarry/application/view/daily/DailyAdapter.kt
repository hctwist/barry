package uk.henrytwist.projectbarry.application.view.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.main.forecast.ForecastElementAdapter
import uk.henrytwist.projectbarry.databinding.DailyRowBinding
import uk.henrytwist.projectbarry.domain.models.DailyForecast

class DailyAdapter(private val handler: Handler) : ListAdapter<DailyAdapter.DayRow, DailyAdapter.Holder>(DailyDiff) {

    override fun getItemCount(): Int {

        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        return Holder(DailyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>) {

        if (payloads.isEmpty()) {

            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.bind(currentList[position], handler)
    }

    class Holder(private val binding: DailyRowBinding) : RecyclerView.ViewHolder(binding.root) {

        private val elementsAdapter = ForecastElementAdapter()

        private var expanded = false

        init {

            binding.dailyElements.layoutManager = LinearLayoutManager(binding.dailyElements.context)
            binding.dailyElements.adapter = elementsAdapter
        }

        fun bind(row: DayRow, handler: Handler) {

            binding.day = row.day
            binding.handler = handler

            elementsAdapter.elements = row.day.elements
            elementsAdapter.notifyDataSetChanged()

            binding.executePendingBindings()

            if (expanded != row.expanded) {

                val newSet = ConstraintSet().apply {

                    clone(itemView.context, if (row.expanded) R.layout.daily_row_expanded_blueprint else R.layout.daily_row)
                }

                newSet.applyTo(binding.root as ConstraintLayout)
            }
        }
    }

    class DayRow(val day: DailyForecast.Day, val expanded: Boolean)

    interface Handler {

        fun onDayRowClick(day: DailyForecast.Day)
    }
}