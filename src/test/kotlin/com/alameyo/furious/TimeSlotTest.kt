package com.alameyo.furious

import com.alameyo.furiouscinema.inputvalidation.TimeSlot
import com.alameyo.furiouscinema.toTime
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

class TimeSlotTest {

    @Test
    fun `When endHour of TimeSlot is before startHour - should throw IllegalArgumentException`() {
        val startTime = "13:00".toTime()
        val endTime = "12:00".toTime()
        assertDoesNotThrow { TimeSlot(endTime, startTime) }
        assertThrows(IllegalArgumentException::class.java) { TimeSlot(startTime, endTime) }
    }

    @Test
    fun `When startHour and endHours are before the compared time - should return false`() {
        val startTime1 = "10:00".toTime()
        val endTime1 = "11:00".toTime()
        val timeSlot1 = TimeSlot(startTime1, endTime1)
        val startTime2 = "12:00".toTime()
        val endTime2 = "13:00".toTime()
        val timeSlot2 = TimeSlot(startTime2, endTime2)

        assertFalse(timeSlot1.isTimeConflict(timeSlot2))
        assertFalse(timeSlot2.isTimeConflict(timeSlot1))
    }

    @Test
    fun `When startHour and endHours interfere - should return true`() {
        val startTime1 = "12:00".toTime()
        val endTime1 = "14:00".toTime()
        val timeSlot1 = TimeSlot(startTime1, endTime1)
        val startTime2 = "13:00".toTime()
        val endTime2 = "15:00".toTime()
        val timeSlot2 = TimeSlot(startTime2, endTime2)

        assertTrue(timeSlot1.isTimeConflict(timeSlot2))
        assertTrue(timeSlot2.isTimeConflict(timeSlot1))
    }

    @Test
    fun `When startHour and endHours are within other timeslot - should return true`() {
        val startTime1 = "12:00".toTime()
        val endTime1 = "15:00".toTime()
        val timeSlot1 = TimeSlot(startTime1, endTime1)
        val startTime2 = "13:00".toTime()
        val endTime2 = "14:00".toTime()
        val timeSlot2 = TimeSlot(startTime2, endTime2)

        assertTrue(timeSlot1.isTimeConflict(timeSlot2))
        assertTrue(timeSlot2.isTimeConflict(timeSlot1))
    }

    @Test
    fun `When endHour of first timeSlot is the same as startHour of next - should return true`() {
        val startTime1 = "12:00".toTime()
        val endTime1 = "13:00".toTime()
        val timeSlot1 = TimeSlot(startTime1, endTime1)
        val startTime2 = "13:00".toTime()
        val endTime2 = "14:00".toTime()
        val timeSlot2 = TimeSlot(startTime2, endTime2)

        assertFalse(timeSlot1.isTimeConflict(timeSlot2))
        assertFalse(timeSlot2.isTimeConflict(timeSlot1))
    }
}
