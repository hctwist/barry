package com.twisthenry8gmail.projectbarry.view.menu

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.core.SavedLocation
import com.twisthenry8gmail.projectbarry.core.SelectedLocation
import com.twisthenry8gmail.projectbarry.databinding.MenuFooterBinding
import com.twisthenry8gmail.projectbarry.databinding.MenuLocationBinding
import com.twisthenry8gmail.projectbarry.view.locations.LocationUtil

class MenuAdapter : RecyclerView.Adapter<MenuAdapter.Holder>() {

    var locations = listOf<SavedLocation>()
    var clickHandler = object : ClickHandler {

        override fun onSettingsClicked() {}
        override fun onLocationSettingsClicked() {}
        override fun onCurrentLocationClicked() {}
        override fun onLocationClicked(location: SavedLocation) {}
    }

    override fun getItemCount(): Int {

        return 2 + locations.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (position) {

            in 0..locations.size -> VIEW_TYPE_LOCATION
            else -> VIEW_TYPE_FOOTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {

        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

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

            is Holder.Location -> {

                val context = holder.itemView.context

                if (position == 0) {

                    val name = holder.itemView.context.getString(R.string.current_location)
                    val icon = ContextCompat.getDrawable(
                        context, LocationUtil.resolveIconRes(
                            SelectedLocation.Status.CURRENT_LOCATION
                        )
                    )
                    holder.bind(MenuLocation(name, icon) {

                        clickHandler.onCurrentLocationClicked()
                    })
                } else {

                    val location = locations[position - 1]
                    val icon = ContextCompat.getDrawable(
                        context,
                        LocationUtil.resolveIconRes(SelectedLocation.Status.STATIC_LOCATION)
                    )
                    holder.bind(MenuLocation(location.name, icon) {

                        clickHandler.onLocationClicked(location)
                    })
                }
            }
            is Holder.Footer -> holder.bind(clickHandler)
        }
    }

    companion object {

        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_LOCATION = 1
        const val VIEW_TYPE_FOOTER = 2
    }

    class MenuLocation(
        val name: String,
        val icon: Drawable?,
        val onClickListener: View.OnClickListener
    )

    sealed class Holder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

//        class Header(private val binding: MenuHeaderBinding) : Holder(binding) {
//
//            fun bind(clickHandler: ClickHandler) {
//
//                binding.clickHandler = clickHandler
//                binding.executePendingBindings()
//            }
//        }

        class Location(private val binding: MenuLocationBinding) : Holder(binding) {

            fun bind(location: MenuLocation) {

                binding.onClickListener = location.onClickListener
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

        fun onCurrentLocationClicked()

        fun onLocationClicked(location: SavedLocation)
    }
}