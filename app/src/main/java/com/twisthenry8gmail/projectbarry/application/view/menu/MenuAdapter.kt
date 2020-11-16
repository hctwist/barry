package com.twisthenry8gmail.projectbarry.application.view.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.twisthenry8gmail.projectbarry.databinding.MenuFooterBinding
import com.twisthenry8gmail.projectbarry.databinding.MenuHeaderBinding
import com.twisthenry8gmail.projectbarry.databinding.MenuLocationBinding
import com.twisthenry8gmail.projectbarry.domain.uicore.MenuLocation

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.Holder>() {

    var locations = listOf<MenuLocation>()
    var clickHandler = object : ClickHandler {

        override fun onSettingsClicked() {}
        override fun onLocationSettingsClicked() {}
        override fun onLocationClicked(location: MenuLocation) {}
    }

    override fun getItemCount(): Int {

        return 2 + locations.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (position) {

            0 -> VIEW_TYPE_HEADER
            in 1..locations.size -> VIEW_TYPE_LOCATION
            else -> VIEW_TYPE_FOOTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            VIEW_TYPE_HEADER -> Holder.Header(MenuHeaderBinding.inflate(inflater, parent, false))

            VIEW_TYPE_LOCATION -> Holder.Location(
                MenuLocationBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_FOOTER -> Holder.Footer(MenuFooterBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        when (holder) {

            is Holder.Header -> {


            }

            is Holder.Location -> {

                holder.bind(clickHandler, locations[position - 1])
            }
            is Holder.Footer -> holder.bind(clickHandler)
        }
    }

    companion object {

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_LOCATION = 1
        const val VIEW_TYPE_FOOTER = 2
    }

    sealed class Holder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        class Header(private val binding: MenuHeaderBinding) : Holder(binding) {

            fun bind() {

                binding.executePendingBindings()
            }
        }

        class Location(private val binding: MenuLocationBinding) : Holder(binding) {

            fun bind(clickHandler: ClickHandler, location: MenuLocation) {

                binding.clickHandler = clickHandler
                binding.location = location
                binding.executePendingBindings()
            }
        }

        class Footer(private val binding: MenuFooterBinding) : Holder(binding) {

            fun bind(clickHandler: ClickHandler) {

                binding.clickHandler = clickHandler
                binding.executePendingBindings()
            }
        }
    }

    interface ClickHandler {

        fun onSettingsClicked()

        fun onLocationSettingsClicked()

        fun onLocationClicked(location: MenuLocation)
    }
}