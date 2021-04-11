package uk.henrytwist.projectbarry.domain.models

enum class WeatherCondition(val group: Group) {

    CLEAR(Group.CLEAR), // Clear
    CLOUDS_FEW(Group.CLEAR), // Few clouds
    CLOUDS_SCATTERED(Group.CLEAR),
    CLOUDS_BROKEN(Group.CLEAR), // Overcast
    CLOUDS_OVERCAST(Group.CLEAR),

    DRIZZLE_LIGHT(Group.RAIN), // Light rain
    DRIZZLE(Group.RAIN), // Rain
    DRIZZLE_HEAVY(Group.RAIN),
    RAIN_LIGHT(Group.RAIN), // Light rain
    RAIN(Group.RAIN), // Rain
    RAIN_HEAVY(Group.RAIN),
    FREEZING_RAIN(Group.RAIN),

    SNOW(Group.SNOW), // Snow
    SNOW_HEAVY(Group.SNOW),
    SNOW_RAIN(Group.SNOW),
    SLEET(Group.SNOW), // Hail?

    THUNDER_DRIZZLE(Group.THUNDER), // Thunder
    THUNDER_DRIZZLE_HEAVY(Group.THUNDER),
    THUNDER_RAIN(Group.THUNDER),
    THUNDER_RAIN_HEAVY(Group.THUNDER),
    THUNDER(Group.THUNDER),

    MIST(Group.VISIBILITY), // Visibility
    SMOKE(Group.VISIBILITY),
    HAZE(Group.VISIBILITY),
    FOG(Group.VISIBILITY),

    DUST(Group.PARTICLES),
    SAND(Group.PARTICLES),
    ASH(Group.PARTICLES),

    SQUALL(Group.EXTREME_WIND), // Wind
    TORNADO(Group.EXTREME_WIND)
    ;

    enum class Group {

        CLEAR, RAIN, THUNDER, SNOW, VISIBILITY, PARTICLES, EXTREME_WIND
    }
}