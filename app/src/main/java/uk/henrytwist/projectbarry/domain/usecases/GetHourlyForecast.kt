package uk.henrytwist.projectbarry.domain.usecases

import uk.henrytwist.kotlinbasics.fractionBetween
import uk.henrytwist.kotlinbasics.maxOfFirst
import uk.henrytwist.kotlinbasics.minOfFirst
import uk.henrytwist.kotlinbasics.outcomes.Outcome
import uk.henrytwist.projectbarry.application.view.hourly.HourlyForecastType
import uk.henrytwist.projectbarry.domain.data.PremiumRepository
import uk.henrytwist.projectbarry.domain.data.SettingsRepository
import uk.henrytwist.projectbarry.domain.data.forecast.Forecast
import uk.henrytwist.projectbarry.domain.data.forecast.ForecastRepository
import uk.henrytwist.projectbarry.domain.models.ForecastElement
import uk.henrytwist.projectbarry.domain.models.HourConditionForecast
import uk.henrytwist.projectbarry.domain.models.HourElementForecast
import uk.henrytwist.projectbarry.domain.models.HourlyForecast
import uk.henrytwist.projectbarry.domain.util.ForecastUtil
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class GetHourlyForecast @Inject constructor(
        private val locationUseCaseHelper: LocationUseCaseHelper,
        private val forecastRepository: ForecastRepository,
        private val settingsRepository: SettingsRepository,
        premiumRepository: PremiumRepository
) {

    private val numberOfHoursToFetch = if (premiumRepository.hasPremium()) 48 else 24

    suspend operator fun invoke(type: HourlyForecastType): Outcome<HourlyForecast> {

        return locationUseCaseHelper.getLocation().switchMap { loc ->

            forecastRepository.get(loc.coordinates).map {

                when (type) {

                    HourlyForecastType.FORECAST -> getConditions(it)
                    HourlyForecastType.TEMPERATURE -> getTemperatureElements(it)
                    HourlyForecastType.POP -> getPopElements(it)
                    HourlyForecastType.UV -> getUVElements(it)
                    HourlyForecastType.WIND_SPEED -> getWindSpeedElements(it)
                }
            }
        }
    }

    private fun getConditions(forecast: Forecast): HourlyForecast.Conditions {

        val blocks = mutableListOf<HourConditionForecast>()

        var startTime = forecast.hourly.first().time.atZone(ZoneId.systemDefault())

        for (i in 1 until numberOfHoursToFetch) {

            val previousHour = forecast.hourly[i - 1]
            val hour = forecast.hourly[i]

            val previousTime = previousHour.time.atZone(ZoneId.systemDefault())
            val time = hour.time.atZone(ZoneId.systemDefault())

            if (previousTime.toLocalDate() != time.toLocalDate()) {

                blocks.add(HourConditionForecast.ConditionBlock(
                        startTime,
                        previousTime,
                        previousHour.condition,
                        ForecastUtil.isNight(startTime, forecast)
                ))
                blocks.add(HourConditionForecast.NewDay(time))

                startTime = time
            } else if (previousHour.condition != hour.condition) {

                blocks.add(HourConditionForecast.ConditionBlock(
                        startTime,
                        previousTime,
                        previousHour.condition,
                        ForecastUtil.isNight(startTime, forecast)
                ))

                startTime = time
            }

            if (i == numberOfHoursToFetch - 1) {

                blocks.add(HourConditionForecast.ConditionBlock(
                        startTime,
                        time,
                        hour.condition,
                        ForecastUtil.isNight(time, forecast)
                ))
            }
        }

        return HourlyForecast.Conditions(blocks)
    }

    private fun getTemperatureElements(forecast: Forecast): HourlyForecast.Elements {

        var suggestedMin = forecast.hourly.minOfFirst(numberOfHoursToFetch) { it.temp.value }
        var suggestedMax = forecast.hourly.maxOfFirst(numberOfHoursToFetch) { it.temp.value }

        val minRange = 10
        val rangeIncrease = max(minRange - (suggestedMax - suggestedMin), 5.0)
        val maxIncreaseWeight = 0F
        val minIncreaseWeight = 1 - maxIncreaseWeight

        suggestedMin -= rangeIncrease * minIncreaseWeight
        suggestedMax += rangeIncrease * maxIncreaseWeight

        val temperatureScale = settingsRepository.getTemperatureScale()

        val builder = object : ElementForecastBuilder(forecast, numberOfHoursToFetch, suggestedMin, suggestedMax) {

            override fun getElement(hour: Forecast.Hour): ForecastElement {

                return ForecastElement.Temperature(hour.temp.to(temperatureScale))
            }

            override fun getValue(hour: Forecast.Hour): Double {

                return hour.temp.value
            }
        }

        return builder.build()
    }

    private fun getPopElements(forecast: Forecast): HourlyForecast.Elements {

        val builder = object : ElementForecastBuilder(forecast, numberOfHoursToFetch, 0.0, 1.0) {

            override fun getElement(hour: Forecast.Hour): ForecastElement {

                return ForecastElement.Pop(hour.pop)
            }

            override fun getValue(hour: Forecast.Hour): Double {

                return hour.pop
            }
        }

        return builder.build()
    }

    private fun getUVElements(forecast: Forecast): HourlyForecast.Elements {

        val uvMax = forecast.hourly.maxOfFirst(numberOfHoursToFetch) { it.uvIndex }
        val suggestedMax = max(uvMax, 7.0)

        val builder = object : ElementForecastBuilder(forecast, numberOfHoursToFetch, 0.0, suggestedMax) {

            override fun getElement(hour: Forecast.Hour): ForecastElement {

                return ForecastElement.UVIndex(hour.uvIndex)
            }

            override fun getValue(hour: Forecast.Hour): Double {

                return hour.uvIndex
            }
        }

        return builder.build()
    }

    private fun getWindSpeedElements(forecast: Forecast): HourlyForecast.Elements {

        val suggestedMax = max(7.5, forecast.hourly.maxOfFirst(numberOfHoursToFetch) { it.windSpeed.metresPerSecond() })

        val windSpeedScale = settingsRepository.getSpeedScale()

        val builder = object : ElementForecastBuilder(forecast, numberOfHoursToFetch, 0.0, suggestedMax) {

            override fun getElement(hour: Forecast.Hour): ForecastElement {

                return ForecastElement.WindSpeed(hour.windSpeed.to(windSpeedScale))
            }

            override fun getValue(hour: Forecast.Hour): Double {

                return hour.windSpeed.metresPerSecond()
            }
        }

        return builder.build()
    }

    abstract class ElementForecastBuilder(private val forecast: Forecast, private val numberOfElements: Int, private val min: Double, private val max: Double) {

        abstract fun getElement(hour: Forecast.Hour): ForecastElement

        abstract fun getValue(hour: Forecast.Hour): Double

        fun build(): HourlyForecast.Elements {

            val n = min(numberOfElements, forecast.hourly.size)
            val elements = ArrayList<HourElementForecast>(numberOfElements)

            var dateCursor: LocalDate? = null

            for (i in 0 until n) {

                val hour = forecast.hourly[i]

                val time = hour.time.atZone(ZoneId.systemDefault())

                if (dateCursor != null && time.toLocalDate() != dateCursor) {

                    elements.add(HourElementForecast.NewDay(time))
                }

                dateCursor = time.toLocalDate()

                elements.add(HourElementForecast.Element(
                        time,
                        getElement(hour),
                        fractionBetween(getValue(hour), min, max))
                )
            }

            return HourlyForecast.Elements(elements)
        }
    }
}