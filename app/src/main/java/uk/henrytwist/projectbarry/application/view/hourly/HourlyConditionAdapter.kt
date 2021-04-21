package uk.henrytwist.projectbarry.application.view.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.HourConditionBlockBinding
import uk.henrytwist.projectbarry.databinding.HourNewDayBinding
import uk.henrytwist.projectbarry.domain.models.HourConditionForecast

class HourlyConditionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var rows = listOf<HourConditionForecast>()

    override fun getItemCount(): Int {

        return rows.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (rows[position]) {

            is HourConditionForecast.ConditionBlock -> VIEW_TYPE_CONDITION
            is HourConditionForecast.NewDay -> VIEW_TYPE_NEW_DAY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            VIEW_TYPE_CONDITION -> ConditionViewHolder(HourConditionBlockBinding.inflate(inflater, parent, false))
            VIEW_TYPE_NEW_DAY -> NewDayViewHolder(HourNewDayBinding.inflate(inflater, parent, false))
            else -> throw RuntimeException("Invalid adapter view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = rows[position]
        when {

            holder is ConditionViewHolder && item is HourConditionForecast.ConditionBlock -> holder.bind(item)
            holder is NewDayViewHolder && item is HourConditionForecast.NewDay -> holder.bind(item.time)
        }
    }

    class ConditionViewHolder(private val binding: HourConditionBlockBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(block: HourConditionForecast.ConditionBlock) {

            binding.block = block
            binding.executePendingBindings()
        }
    }

    companion object {

        const val VIEW_TYPE_CONDITION = 0
        const val VIEW_TYPE_NEW_DAY = 1
    }
}