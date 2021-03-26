package uk.henrytwist.projectbarry.application.view.locations

import androidx.recyclerview.widget.DiffUtil
import uk.henrytwist.projectbarry.domain.models.SavedLocation

object SavedLocationDiff : DiffUtil.ItemCallback<SavedLocation>() {

    override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {

        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {

        return oldItem.pinned == newItem.pinned &&
                oldItem.name == newItem.name
    }
}