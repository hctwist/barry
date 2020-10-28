package com.twisthenry8gmail.projectbarry.core

enum class WeatherCondition(val group: Group) {

    CLEAR(Group.CLEAR),
    CLOUDS_FEW(Group.CLEAR),
    CLOUDS_SCATTERED(Group.CLEAR),
    CLOUDS_BROKEN(Group.CLEAR),
    CLOUDS_OVERCAST(Group.CLEAR),

    DRIZZLE_LIGHT(Group.RAIN),
    DRIZZLE(Group.RAIN),
    DRIZZLE_HEAVY(Group.RAIN),
    RAIN_LIGHT(Group.RAIN),
    RAIN(Group.RAIN),
    RAIN_HEAVY(Group.RAIN),
    FREEZING_RAIN(Group.RAIN),

    SNOW(Group.SNOW),
    SNOW_HEAVY(Group.SNOW),
    SNOW_RAIN(Group.SNOW),
    SLEET(Group.SNOW),

    THUNDER_DRIZZLE(Group.THUNDER),
    THUNDER_DRIZZLE_HEAVY(Group.THUNDER),
    THUNDER_RAIN(Group.THUNDER),
    THUNDER_RAIN_HEAVY(Group.THUNDER),
    THUNDER(Group.THUNDER),

    MIST(Group.VISIBILITY),
    SMOKE(Group.VISIBILITY),
    HAZE(Group.VISIBILITY),
    FOG(Group.VISIBILITY),

    DUST(Group.PARTICLES),
    SAND(Group.PARTICLES),
    ASH(Group.PARTICLES),

    SQUALL(Group.WIND),
    TORNADO(Group.WIND)
    ;

    enum class Group {

        CLEAR, RAIN, THUNDER, SNOW, VISIBILITY, PARTICLES, WIND
    }
}