package com.alameyo.furiouscinema.inputvalidation

import java.time.LocalTime

class TimeSlot(private val startHour: LocalTime, private val endHour: LocalTime) {
    init {
        require(!endHour.isBefore(startHour))
    }

    fun isTimeConflict(comparedTimeSlot: TimeSlot): Boolean {
        return when {
            this.startHour.isAfter(comparedTimeSlot.endHour) -> false
            comparedTimeSlot.startHour.isAfter(this.endHour) -> false

            this.startHour == comparedTimeSlot.endHour -> false
            comparedTimeSlot.startHour == this.endHour -> false

            this.startHour.isBefore(comparedTimeSlot.endHour) && this.endHour.isAfter(comparedTimeSlot.startHour) -> true
            comparedTimeSlot.startHour.isBefore(this.endHour) && comparedTimeSlot.endHour.isAfter(comparedTimeSlot.startHour) -> true
            else -> false
        }
    }
}
