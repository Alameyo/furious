package com.alameyo.furiouscinema.repositories

import com.alameyo.furiouscinema.JsonFields.DATE
import com.alameyo.furiouscinema.JsonFields.END_HOUR
import com.alameyo.furiouscinema.JsonFields.MOVIE_ID
import com.alameyo.furiouscinema.JsonFields.PRICE
import com.alameyo.furiouscinema.JsonFields.ROOM
import com.alameyo.furiouscinema.JsonFields.START_HOUR
import com.alameyo.furiouscinema.JsonFields.TIME_SLOTS
import com.alameyo.furiouscinema.JsonFields.TIME_TABLES
import com.alameyo.furiouscinema.getCollection
import com.google.gson.JsonObject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.ReplaceOptions
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimeTableRepository(@Autowired private val database: MongoDatabase) {
    private val timeTables = database.getCollection(TIME_TABLES)

    fun getTimeTable(date: String = today()): Document? = timeTables.find(eq(DATE.fieldName, date)).projection(excludeId()).first()

    fun createTimeTableDocument(timeTable: JsonObject): Document {
        val date = timeTable[DATE.fieldName].asString
        val listOfTimeSlots = timeTable.getAsJsonArray(TIME_SLOTS.fieldName).toList()
        val bsonTimeSlots = mutableListOf<Document>()

        bsonTimeSlots.apply {
            listOfTimeSlots.forEach {
                add(toTimeSlotDocument(it as JsonObject))
            }
        }

        return Document().apply {
            append(DATE.fieldName, date)
            append(TIME_SLOTS.fieldName, bsonTimeSlots)
        }
    }

    fun commitTimeTable(timeTableDocument: Document): Boolean {
        val date = timeTableDocument[DATE.fieldName].toString()
        val exist = getTimeTable(date)?.isNotEmpty() ?: false
        val replaceUpsertOption = ReplaceOptions().upsert(true)
        val result = timeTables.replaceOne(eq(DATE.fieldName, date), timeTableDocument, replaceUpsertOption)
        return result.wasAcknowledged() && !exist
    }

    private fun toTimeSlotDocument(it: JsonObject) = Document().apply {
        append(ROOM.fieldName, it[ROOM.fieldName].asInt)
        append(MOVIE_ID.fieldName, it[MOVIE_ID.fieldName].asString)
        append(START_HOUR.fieldName, it[START_HOUR.fieldName].asString)
        append(END_HOUR.fieldName, it[END_HOUR.fieldName].asString)
        append(PRICE.fieldName, it[PRICE.fieldName].asString)
    }

    private fun today() = ofPattern("yyyy-MM-dd").format(now())
}
