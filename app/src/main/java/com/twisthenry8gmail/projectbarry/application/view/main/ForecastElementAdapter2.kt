package com.twisthenry8gmail.projectbarry.application.view.main

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.twisthenry8gmail.projectbarry.domain.core.ForecastElement

class ForecastElementAdapter2 : BaseAdapter() {

    var elements = listOf<ForecastElement>()

    override fun getCount(): Int {

        return elements.size
    }

    override fun getItem(position: Int): Any {

        return Unit
    }

    override fun getItemId(position: Int): Long {

        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = ForecastElementView(parent!!.context)
        view.setElement(elements[position])

        return view
    }


}