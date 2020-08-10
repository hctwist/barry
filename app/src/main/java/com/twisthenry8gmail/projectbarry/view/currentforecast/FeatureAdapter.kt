package com.twisthenry8gmail.projectbarry.view.currentforecast

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FeatureAdapter : RecyclerView.Adapter<FeatureAdapter.VH>() {

    var features = listOf<Feature<*>>()

    override fun getItemCount(): Int {

        return features.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(FeatureBoxView(parent.context))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.featureBoxView.setFeature(features[position])
    }

    class VH(val featureBoxView: FeatureBoxView) : RecyclerView.ViewHolder(featureBoxView)
}