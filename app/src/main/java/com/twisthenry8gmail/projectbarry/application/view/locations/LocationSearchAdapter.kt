package com.twisthenry8gmail.projectbarry.application.view.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.domain.core.LocationSearchResult
import com.twisthenry8gmail.projectbarry.databinding.LocationSearchRowBinding

class LocationSearchAdapter : RecyclerView.Adapter<LocationSearchAdapter.VH>() {

    var places = listOf<LocationSearchResult>()

    lateinit var clickListener: ClickListener

    override fun getItemCount(): Int {

        return places.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationSearchRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(places[position], clickListener)
    }

    class VH(private val binding: LocationSearchRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: LocationSearchResult, clickListener: ClickListener) {

            binding.result = result
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }
    }

    interface ClickListener {

        fun onClick(result: LocationSearchResult)
    }
}