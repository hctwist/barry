package com.twisthenry8gmail.projectbarry.data

enum class WeatherCondition(val group: Group) {

    NOTHING(Group.OTHER),

    CLEAR(Group.CLEAR),
    CLOUDS_FEW(Group.CLEAR),
    CLOUDS_SCATTERED(Group.CLEAR),
    CLOUDS_BROKEN(Group.CLEAR),
    CLOUDS_OVERCAST(Group.CLEAR),

    THUNDER_DRIZZLE_LIGHT(Group.RAIN),
    THUNDER_DRIZZLE(Group.RAIN),
    THUNDER_DRIZZLE_HEAVY(Group.RAIN),
    DRIZZLE_LIGHT(Group.RAIN),
    DRIZZLE(Group.RAIN),
    DRIZZLE_HEAVY(Group.RAIN),
    RAIN_LIGHT(Group.RAIN),
    RAIN(Group.RAIN),
    RAIN_HEAVY(Group.RAIN),

    SNOW(Group.SNOW),
    SNOW_HEAVY(Group.SNOW),
    SNOW_RAIN_LIGHT(Group.SNOW),
    SNOW_RAIN(Group.SNOW),

    THUNDER_RAIN_LIGHT(Group.THUNDER),
    THUNDER_RAIN(Group.THUNDER),
    THUNDER_RAIN_HEAVY(Group.THUNDER),
    THUNDER(Group.THUNDER);

    enum class Group {

        OTHER, CLEAR, RAIN, THUNDER, SNOW
    }
}