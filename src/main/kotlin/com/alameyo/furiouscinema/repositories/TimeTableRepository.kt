package com.alameyo.furiouscinema.repositories

import com.google.gson.JsonObject
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ofPattern

@Component
class TimeTableRepository(@Autowired private val database: MongoDatabase) {
    private val timeTables = database.getCollection("timeTables")

    fun getTimeTable(date: String = today()): Document? = timeTables.find(eq("date", date)).projection(excludeId()).first()

    fun createTimeTableDocument(timeTable: JsonObject): Document {
        val date = timeTable["date"].asString
        val listOfTimeSlots = timeTable.getAsJsonArray("timeSlots").toList()
        val bsonTimeSlots = mutableListOf<Document>()

        bsonTimeSlots.apply {
            listOfTimeSlots.forEach {
                add(toTimeSlotDocument(it as JsonObject))
            }
        }

        return Document().apply {
            append("date", date)
            append("timeSlots", bsonTimeSlots)
        }
    }

    fun commitTimeTable(timeTableDocument: Document): Boolean {
        val date = timeTableDocument["date"].toString()
        val exist = getTimeTable(date)?.isNotEmpty() ?: false
        val replaceUpsertOption = ReplaceOptions().upsert(true)
        val result = timeTables.replaceOne(eq("date", date), timeTableDocument, replaceUpsertOption)
        return result.wasAcknowledged() && !exist
    }

    private fun toTimeSlotDocument(it: JsonObject) = Document().apply {
        append("room", it["room"].asString)
        append("movieId", it["movieId"].asString)
        append("startHour", it["startHour"].asString)
        append("endHour", it["endHour"].asString)
        append("price", it["price"].asString)
    }

    private fun today() = ofPattern("yyyy-MM-dd").format(now())
}
