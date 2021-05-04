package uk.henrytwist.projectbarry.application.view.daily

import androidx.recyclerview.widget.DiffUtil

object DailyDiff : DiffUtil.ItemCallback<DailyAdapter.DayRow>() {

    override fun areItemsTheSame(oldItem: DailyAdapter.DayRow, newItem: DailyAdapter.DayRow): Boolean {

        return oldItem.day.date == newItem.day.date
    }

    override fun areContentsTheSame(oldItem: DailyAdapter.DayRow, newItem: DailyAdapter.DayRow): Boolean {

        return oldItem.expanded == newItem.expanded
    }

    override fun getChangePayload(oldItem: DailyAdapter.DayRow, newItem: DailyAdapter.DayRow): Any? {

        return Expansion(newItem.expanded)
    }

    class Expansion(val expand: Boolean)
}