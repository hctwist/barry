package uk.henrytwist.projectbarry.application.view.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uk.henrytwist.projectbarry.databinding.LocationSearchRowBinding
import uk.henrytwist.projectbarry.domain.models.LocationSearchResult

class LocationSearchAdapter(private val handler: Handler) : RecyclerView.Adapter<LocationSearchAdapter.VH>() {

    var searchResults = listOf<LocationSearchResult>()

    override fun getItemCount(): Int {

        return searchResults.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationSearchRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(searchResults[position], handler)
    }

    class VH(private val binding: LocationSearchRowBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(result: LocationSearchResult, handler: Handler) {

            binding.result = result
            binding.handler = handler
            binding.executePendingBindings()
        }
    }

    interface Handler {

        fun onClickSearchResult(result: LocationSearchResult, pin: Boolean)
    }
}