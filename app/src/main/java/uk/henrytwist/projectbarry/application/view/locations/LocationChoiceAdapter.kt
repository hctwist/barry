package uk.henrytwist.projectbarry.application.view.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.LocationRowBinding
import uk.henrytwist.projectbarry.domain.models.SavedLocation

class LocationChoiceAdapter(private val handler: Handler) : ListAdapter<SavedLocation, LocationChoiceAdapter.VH>(SavedLocationDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(getItem(position), handler)
    }

    class VH(private val binding: LocationRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: SavedLocation, handler: Handler) {

            binding.location = location
            binding.clicklistener = handler

            binding.locationPin.setOnClickListener {

                binding.locationPin.toggle()
                handler.onPinLocation(location)
            }

            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onChooseLocation(location: SavedLocation)

        fun onPinLocation(location: SavedLocation)
    }
}