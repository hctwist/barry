package com.twisthenry8gmail.projectbarry.application.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.domain.core.DaySnapshot
import com.twisthenry8gmail.projectbarry.databinding.DaySnapshotRowBinding

class DaySnapshotAdapter: RecyclerView.Adapter<DaySnapshotAdapter.VH>() {

    var snapshots = listOf<DaySnapshot>()

    override fun getItemCount(): Int {

        return snapshots.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(
            DaySnapshotRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(snapshots[position])
    }

    class VH(private val binding: DaySnapshotRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: DaySnapshot) {

            binding.snapshot = snapshot
            binding.executePendingBindings()
        }
    }
}