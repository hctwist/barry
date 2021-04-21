package uk.henrytwist.projectbarry.application.view.hourly

import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.HourNewDayBinding
import java.time.ZonedDateTime

class NewDayViewHolder(private val binding: HourNewDayBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(time: ZonedDateTime) {

        binding.time = time
        binding.executePendingBindings()
    }
}