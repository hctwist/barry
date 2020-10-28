package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.core.HourSnapshot
import com.twisthenry8gmail.projectbarry.databinding.HourlySnapshotRowBinding

class HourSnapshotAdapter : RecyclerView.Adapter<HourSnapshotAdapter.VH>() {

    var snapshots = listOf<HourSnapshot>()

    override fun getItemCount(): Int {

        return snapshots.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(
            HourlySnapshotRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(snapshots[position])
    }

    class VH(private val binding: HourlySnapshotRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(snapshot: HourSnapshot) {

            binding.snapshot = snapshot
            binding.executePendingBindings()
        }
    }
}