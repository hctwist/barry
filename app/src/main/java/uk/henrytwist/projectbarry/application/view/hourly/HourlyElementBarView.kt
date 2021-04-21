package uk.henrytwist.projectbarry.application.view.hourly

import android.content.Context
import android.util.AttributeSet
import uk.henrytwist.projectbarry.application.view.components.TagView
import uk.henrytwist.projectbarry.application.view.resolvers.ForecastElementResolver
import uk.henrytwist.projectbarry.application.view.resolvers.TagResolver
import uk.henrytwist.projectbarry.domain.models.HourElementForecast

class HourlyElementBarView(context: Context, attributeSet: AttributeSet) : TagView(context, attributeSet) {

    private var fraction = 0F

    init {

        textAlignment = TEXT_ALIGNMENT_VIEW_END
    }

    fun setElement(element: HourElementForecast.Element) {

        val valueString = ForecastElementResolver.getElementDisplayString(context, element.element)
        val tagColor = TagResolver.resolveTag(context, element.element)!!.color

        setTagContents(TagContents(valueString, tagColor))

        fraction = element.fraction.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)
        minimumWidth = (maxWidth * fraction).toInt()

        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec)
    }
}