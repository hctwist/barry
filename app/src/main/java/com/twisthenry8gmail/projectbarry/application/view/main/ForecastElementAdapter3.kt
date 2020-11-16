package com.twisthenry8gmail.projectbarry.application.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.twisthenry8gmail.projectbarry.domain.core.ForecastElement

class ForecastElementAdapter3 : ExpanderLinearLayout.Adapter<ForecastElementAdapter3.Box>() {

    var elements = listOf<ForecastElement>()

    override fun getItemCount(): Int {

        return elements.size
    }

    override fun createViewBox(
        position: Int,
        parent: ViewGroup,
        layoutInflater: LayoutInflater
    ): Box {

        return Box(ForecastElementView(parent.context))
    }

    override fun bindViewBox(position: Int, viewBox: Box) {

        viewBox.elementView.setElement(elements[position])
    }

    class Box(val elementView: ForecastElementView) : ExpanderLinearLayout.ViewBox(elementView)
}