package uk.henrytwist.projectbarry.application.view.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.LocationRowBinding
import uk.henrytwist.projectbarry.domain.models.SavedLocation

class LocationChoiceAdapter : ListAdapter<SavedLocation, LocationChoiceAdapter.VH>(SavedLocationDiff) {

    lateinit var clickHandler: ClickHandler

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(getItem(position), clickHandler)
    }

    class VH(private val binding: LocationRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: SavedLocation, clickHandler: ClickHandler) {

            binding.location = location
            binding.clicklistener = clickHandler

            binding.locationPin.setOnClickListener {

                binding.locationPin.toggle()
                clickHandler.onPin(location)
            }

            binding.executePendingBindings()
        }
    }

    interface ClickHandler {

        fun onClick(location: SavedLocation)

        fun onPin(location: SavedLocation)
    }
}