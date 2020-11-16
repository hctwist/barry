package com.twisthenry8gmail.projectbarry.application.view.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.domain.core.SavedLocation
import com.twisthenry8gmail.projectbarry.databinding.LocationRowBinding

class LocationChoiceAdapter : RecyclerView.Adapter<LocationChoiceAdapter.VH>() {

    var rows = listOf<SavedLocation>()

    lateinit var clickHandler: ClickHandler

    override fun getItemCount(): Int {

        return rows.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(rows[position], clickHandler)
    }

    class VH(private val binding: LocationRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: SavedLocation, clickHandler: ClickHandler) {

            binding.location = location
            binding.clicklistener = clickHandler
            binding.executePendingBindings()
        }
    }

    interface ClickHandler {

        fun onClick(location: SavedLocation)

        fun onPin(location: SavedLocation)
    }
}