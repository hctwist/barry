package uk.henrytwist.projectbarry.application.view.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uk.henrytwist.androidbasics.livedata.immutable
import uk.henrytwist.androidbasics.navigation.NavigatorViewModel
import uk.henrytwist.kotlinbasics.findIndex
import uk.henrytwist.projectbarry.application.view.components.HeaderAdapter
import uk.henrytwist.projectbarry.domain.models.DailyForecast
import uk.henrytwist.projectbarry.domain.usecases.GetDailyForecast
import java.time.LocalDate
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DailyViewModel @Inject constructor(
        private val getDailyForecast: GetDailyForecast
) : NavigatorViewModel(), HeaderAdapter.Handler, DailyAdapter.Handler {

    private val _forecast = MutableLiveData<List<DailyAdapter.DayRow>>()
    val forecast = _forecast.immutable()

    init {

        viewModelScope.launch {

            getDailyForecast().ifSuccessful { f ->

                val now = LocalDate.now()
                _forecast.value = f.days.map {

                    DailyAdapter.DayRow(it, it.date.toLocalDate() == now)
                }
            }
        }
    }

    override fun onClickBack() {

        navigateBack()
    }

    override fun onDayRowClick(day: DailyForecast.Day) {

        _forecast.value?.let { currentList ->

            val newList = currentList.toMutableList()
            val clickedIndex = newList.findIndex { it.day.date == day.date } ?: return
            val wasExpanded = newList[clickedIndex].expanded

            clearExpanded(newList)

            if (wasExpanded) {

                newList[0] = DailyAdapter.DayRow(newList[0].day, true)
            } else {

                newList[clickedIndex] = DailyAdapter.DayRow(newList[clickedIndex].day, true)
            }

            _forecast.value = newList
        }
    }

    private fun clearExpanded(rows: MutableList<DailyAdapter.DayRow>) {

        for (i in rows.indices) {

            if (rows[i].expanded) rows[i] = DailyAdapter.DayRow(rows[i].day, false)
        }
    }
}