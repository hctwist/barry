package uk.henrytwist.projectbarry.application.view.main

import uk.henrytwist.androidbasics.recyclerview.BindingItemAdapter
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.databinding.MainHourSnapshotRowBinding
import uk.henrytwist.projectbarry.domain.models.NowForecast

class HourSnapshotAdapter : BindingItemAdapter<MainHourSnapshotRowBinding>(R.layout.main_hour_snapshot_row) {

    var snapshots = listOf<NowForecast.HourSnapshot>()

    override fun onBind(binding: MainHourSnapshotRowBinding, position: Int) {

        binding.snapshot = snapshots[position]
    }

    override fun getItemCount(): Int {

        return snapshots.size
    }


}