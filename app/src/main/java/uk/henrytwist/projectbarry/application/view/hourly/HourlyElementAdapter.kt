package uk.henrytwist.projectbarry.application.view.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.HourElementRowBinding
import uk.henrytwist.projectbarry.databinding.HourNewDayBinding
import uk.henrytwist.projectbarry.domain.models.HourElementForecast

class HourlyElementAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var rows = listOf<HourElementForecast>()

    override fun getItemCount(): Int {

        return rows.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (rows[position]) {

            is HourElementForecast.Element -> VIEW_TYPE_ELEMENT
            is HourElementForecast.NewDay -> VIEW_TYPE_NEW_DAY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            VIEW_TYPE_ELEMENT -> ElementViewHolder(HourElementRowBinding.inflate(inflater, parent, false))
            VIEW_TYPE_NEW_DAY -> NewDayViewHolder(HourNewDayBinding.inflate(inflater, parent, false))
            else -> throw RuntimeException("Invalid adapter view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = rows[position]
        when {

            holder is ElementViewHolder && item is HourElementForecast.Element -> holder.bind(item)
            holder is NewDayViewHolder && item is HourElementForecast.NewDay -> holder.bind(item.time)
        }
    }

    class ElementViewHolder(private val binding: HourElementRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(element: HourElementForecast.Element) {

            binding.element = element
            binding.executePendingBindings()
        }
    }

    companion object {

        const val VIEW_TYPE_ELEMENT = 0
        const val VIEW_TYPE_NEW_DAY = 1
    }
}