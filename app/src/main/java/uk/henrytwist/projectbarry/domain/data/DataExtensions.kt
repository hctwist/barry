package uk.henrytwist.projectbarry.domain.data

import java.time.Instant

fun Long.toInstant(): Instant = Instant.ofEpochSecond(this)