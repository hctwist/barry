package com.twisthenry8gmail.projectbarry.application.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.twisthenry8gmail.projectbarry.databinding.HourlySnapshotRowBinding
import com.twisthenry8gmail.projectbarry.domain.core.HourSnapshot

class HourSnapshotAdapter2 : ExpanderLinearLayout.Adapter<HourSnapshotAdapter2.Box>() {

    var snapshots = listOf<HourSnapshot>()

    override fun getItemCount(): Int {

        return snapshots.size
    }

    override fun createViewBox(
        position: Int,
        parent: ViewGroup,
        layoutInflater: LayoutInflater
    ): Box {

        return Box(
            HourlySnapshotRowBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun bindViewBox(position: Int, viewBox: Box) {

        viewBox.binding.snapshot = snapshots[position]
        viewBox.binding.executePendingBindings()
    }

    class Box(val binding: HourlySnapshotRowBinding) : ExpanderLinearLayout.ViewBox(binding.root)
}