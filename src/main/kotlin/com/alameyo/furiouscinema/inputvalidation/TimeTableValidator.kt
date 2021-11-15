package com.alameyo.furiouscinema.inputvalidation

import com.alameyo.furiouscinema.JsonFields.DATE
import com.alameyo.furiouscinema.JsonFields.END_HOUR
import com.alameyo.furiouscinema.JsonFields.MOVIE_ID
import com.alameyo.furiouscinema.JsonFields.PRICE
import com.alameyo.furiouscinema.JsonFields.ROOM
import com.alameyo.furiouscinema.JsonFields.START_HOUR
import com.alameyo.furiouscinema.JsonFields.TIME_SLOTS
import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.toTime
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import java.time.format.DateTimeParseException
import org.springframework.stereotype.Component

@Component
class TimeTableValidator : FuriousValidator {
    private val movieIdValidator = MovieIdValidator()
    private val dateValidator = DateValidator()

    override fun validate(value: String) {
        try {
            val jsonTimeTable = value.asJsonObject()
            checkIfRoomsParseToInt(jsonTimeTable)
            checkIfMovieIdAreValid(jsonTimeTable)
            checkIfRequiredFieldsExists(jsonTimeTable)
            checkIfTimesAreProperlyFormatted(jsonTimeTable)
            checkForTimeConflicts(jsonTimeTable)
        } catch (e: JsonParseException) {
            throw InputValidationException()
        }
    }

    @Suppress("ThrowsCount")
    private fun checkIfRequiredFieldsExists(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable[TIME_SLOTS.fieldName].asJsonArray ?: throw InputValidationException()
        timeslots.forEach {
            if (it is JsonObject) {
                it[ROOM.fieldName] ?: throw InputValidationException()
                it[MOVIE_ID.fieldName] ?: throw InputValidationException()
                it[START_HOUR.fieldName]?.asString ?: throw InputValidationException()
                it[END_HOUR.fieldName]?.asString ?: throw InputValidationException()
                it[PRICE.fieldName]?.asString ?: throw InputValidationException()
            } else throw InputValidationException()
        }
    }

    private fun checkIfRoomsParseToInt(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable[TIME_SLOTS.fieldName].asJsonArray ?: throw InputValidationException()
        timeslots.forEach {
            if (it is JsonObject) {
                try {
                    it[ROOM.fieldName].asInt
                } catch (exception: NumberFormatException) {
                    throw InputValidationException()
                }
            }
        }
    }

    private fun checkIfTimesAreProperlyFormatted(jsonTimeTable: JsonObject) {
        val dateJson = jsonTimeTable[DATE.fieldName].asString
        val timeslots = jsonTimeTable[TIME_SLOTS.fieldName].asJsonArray
        try {
            dateValidator.validate(dateJson)
            timeslots.forEach {
                it as JsonObject
                it[START_HOUR.fieldName].asString.toTime()
                it[END_HOUR.fieldName].asString.toTime()
            }
        } catch (exception: DateTimeParseException) {
            throw InputValidationException()
        }
    }

    private fun checkIfMovieIdAreValid(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable[TIME_SLOTS.fieldName].asJsonArray
        timeslots.forEach {
            if (it is JsonObject) {
                movieIdValidator.validate(it[MOVIE_ID.fieldName].asString)
            }
        }
    }

    private fun checkForTimeConflicts(jsonTimeTable: JsonObject) {
        val timeslots: JsonArray = jsonTimeTable[TIME_SLOTS.fieldName].asJsonArray
        val sortedTimeslots = timeslots.sortedBy { (it as JsonObject)[START_HOUR.fieldName].asString.toTime() }
        val roomMap = mutableMapOf<Int, TimeSlot>()
        sortedTimeslots.forEach {
            it as JsonObject
            val room = it[ROOM.fieldName].asInt
            val startHour = it[START_HOUR.fieldName].asString.toTime()
            val endHour = it[END_HOUR.fieldName].asString.toTime()
            val timeSlot = TimeSlot(startHour, endHour)
            if (roomMap[room] == null) {
                roomMap[room] = timeSlot
            } else if (roomMap[room]?.isTimeConflict(timeSlot) == true) {
                throw InputValidationException()
            }
        }
    }
}

