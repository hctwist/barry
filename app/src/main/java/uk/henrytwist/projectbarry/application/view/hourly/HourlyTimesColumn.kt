package uk.henrytwist.projectbarry.application.view.hourly

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import uk.henrytwist.projectbarry.R
import uk.henrytwist.projectbarry.application.view.TimeDisplayUtil
import java.time.ZonedDateTime

class HourlyTimesColumn(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {

    init {

        orientation = VERTICAL
    }

    fun setTimes(times: List<ZonedDateTime>) {

        if (times.size > childCount) {

            repeat(times.size - childCount) {

                addTime()
            }
        } else if (times.size < childCount) {

            for (i in (times.size - 1) until childCount) {

                getChildAt(i).visibility = GONE
            }
        }

        for (i in times.indices) {

            val textView = (getChildAt(i) as FrameLayout).getChildAt(0) as TextView
            textView.text = TimeDisplayUtil.displayMeridiemHour(times[i])
        }
    }

    private fun addTime() {

        inflate(context, R.layout.hour_time_column_row, this)
    }
}