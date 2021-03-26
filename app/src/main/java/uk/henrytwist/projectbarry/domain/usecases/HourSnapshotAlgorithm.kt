package uk.henrytwist.projectbarry.domain.usecases

class HourSnapshotAlgorithm {

//    val hourSnapshots = ArrayList<HourSnapshot>(nHourSnapshots)
//
//    val nextHour = if (abs(forecast.hourly[0].time.until(now, ChronoUnit.MINUTES)) > 40 || forecast.hourly[0].condition.group == forecast.condition.group
//    ) {
//
//        forecast.hourly[1]
//    } else {
//
//        forecast.hourly[0]
//    }
//    val nextHourCondition = nextHour.condition
//    hourSnapshots.add(
//    nextHour.toSnapshot(nextHourCondition, forecast.condition.group != nextHourCondition.group)
//    )
//
//    var currentHourSnapshots = 1
//    var lastHourSnapshotCondition = nextHourCondition
//    var hourGap = 0
//    for (i in 1 until forecast.hourly.size) {
//
//        hourGap++
//
//        val hour = forecast.hourly[i]
//
//        if (hour.condition.group != lastHourSnapshotCondition.group) {
//
//            hourSnapshots.add(hour.toSnapshot(hour.condition, true))
//            currentHourSnapshots++
//            lastHourSnapshotCondition = hour.condition
//            hourGap = 0
//        } else if (hourGap == 3) {
//
//            hourSnapshots.add(hour.toSnapshot(hour.condition, false))
//            currentHourSnapshots++
//            lastHourSnapshotCondition = hour.condition
//            hourGap = 0
//        }
//
//        if (currentHourSnapshots == nHourSnapshots) break
//    }
}