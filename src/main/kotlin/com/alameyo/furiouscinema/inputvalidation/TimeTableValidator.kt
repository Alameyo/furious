package com.alameyo.furiouscinema.inputvalidation

import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.toDate
import com.alameyo.furiouscinema.toTime
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import java.time.format.DateTimeParseException
import org.springframework.stereotype.Component

@Component
class TimeTableValidator : FuriousValidator {
    private val movieIdValidator = MovieIdValidator()

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

    private fun checkIfRequiredFieldsExists(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable["timeSlots"].asJsonArray ?: throw InputValidationException()
        timeslots.forEach {
            if (it is JsonObject) {
                it["room"] ?: throw InputValidationException()
                it["movieId"] ?: throw InputValidationException()
                it["startHour"]?.asString ?: throw InputValidationException()
                it["endHour"]?.asString
                it["price"]?.asString ?: throw InputValidationException()
            } else throw InputValidationException()
        }
    }

    private fun checkIfRoomsParseToInt(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable["timeSlots"].asJsonArray ?: throw InputValidationException()
        timeslots.forEach {
            if (it is JsonObject) {
                try {
                    it["room"].asInt
                } catch (exception: NumberFormatException) {
                    throw InputValidationException()
                }
            }
        }
    }

    private fun checkIfTimesAreProperlyFormatted(jsonTimeTable: JsonObject) {
        val dateJson = jsonTimeTable["date"]
        val timeslots = jsonTimeTable["timeSlots"].asJsonArray
        try {
            dateJson.asString.toDate()
            timeslots.forEach {
                it as JsonObject
                it["startHour"].asString.toTime()
                it["endHour"].asString.toTime()
            }
        } catch (exception: DateTimeParseException) {
            throw InputValidationException()
        }
    }

    private fun checkIfMovieIdAreValid(jsonTimeTable: JsonObject) {
        val timeslots = jsonTimeTable["timeSlots"].asJsonArray
        timeslots.forEach {
            if (it is JsonObject) {
                movieIdValidator.validate(it["movieId"].asString)
            }
        }
    }

    private fun checkForTimeConflicts(jsonTimeTable: JsonObject) {
        val timeslots: JsonArray = jsonTimeTable["timeSlots"].asJsonArray
        val sortedTimeslots = timeslots.sortedBy { (it as JsonObject)["startHour"].asString.toTime() }
        val roomMap = mutableMapOf<Int, TimeSlot>()
        sortedTimeslots.forEach {
            it as JsonObject
            val room = it["room"].asInt
            val startHour = it["startHour"].asString.toTime()
            val endHour = it["endHour"].asString.toTime()
            val timeSlot = TimeSlot(startHour, endHour)
            if (roomMap[room] == null) {
                roomMap[room] = timeSlot
            } else if (roomMap[room]?.isTimeConflict(timeSlot) == true) {
                throw InputValidationException()
            }
        }
    }
}

