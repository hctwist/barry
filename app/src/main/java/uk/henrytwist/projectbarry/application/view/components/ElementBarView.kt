package uk.henrytwist.projectbarry.application.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import uk.henrytwist.projectbarry.domain.models.ForecastElement

class ElementBarView(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    fun setElement(element: ForecastElement) {

        element.tagRange.start
    }
}