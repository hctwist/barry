package com.twisthenry8gmail.projectbarry.view.locations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.twisthenry8gmail.projectbarry.R
import com.twisthenry8gmail.projectbarry.data.locations.ForecastLocation
import com.twisthenry8gmail.projectbarry.databinding.LocationRowBinding

class LocationChoiceAdapter : RecyclerView.Adapter<LocationChoiceAdapter.VH>() {

    var choices = listOf<Choice>()

    lateinit var clickListener: ClickListener

    override fun getItemCount(): Int {

        return choices.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        val inflater = LayoutInflater.from(parent.context)
        return VH(LocationRowBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.bind(choices[position], clickListener)
    }

    class VH(private val binding: LocationRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(choice: Choice, clickListener: ClickListener) {

            binding.choice = choice
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }
    }

    interface ClickListener {

        fun onClick(choice: Choice)

        fun onPin(choice: Choice)
    }

    sealed class Choice(val type: ForecastLocation.Type) {

        fun getIcon(context: Context) = context.getDrawable(

            LocationUtil.resolveIconRes(type)
        )

        abstract fun getTitle(context: Context): String

        object CurrentLocation : Choice(ForecastLocation.Type.CURRENT_LOCATION) {

            override fun getTitle(context: Context): String {

                return context.getString(R.string.current_location)
            }
        }

        class Location(val placeId: String, type: ForecastLocation.Type, val title: String) :
            Choice(type) {

            override fun getTitle(context: Context): String {

                return title
            }
        }
    }
}