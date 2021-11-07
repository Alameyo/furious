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

    fun getTimeTable(date: String = today()): Document? {
        return timeTables.find(eq("date", date)).projection(excludeId()).first()
    }

    fun getTimeTables(): List<Document> {
        return timeTables.find().projection(excludeId()).toList()
    }

    fun createOrReplaceTimeTable(timeTable: JsonObject) {
        val bsonTimeSlots = mutableListOf<Document>()
        val list = timeTable.getAsJsonArray("timeSlots").toList()
        val date = timeTable["date"].asString
        list.forEach {
            val bsonTimeSlot = Document().apply {
                it as JsonObject
                append("room", (it).get("room").asString)
                append("movieId", (it).get("movieId").asString)
                append("startHour", (it).get("startHour").asString)
                append("endHour", (it).get("endHour").asString)
            }
            bsonTimeSlots.add(bsonTimeSlot)
        }
        val timeTableDocument = Document().apply {
            append("date", date)
            append("timeSlots", bsonTimeSlots)
        }
        val replaceUpsertOption = ReplaceOptions().upsert(true)
        timeTables.replaceOne(eq("date", date), timeTableDocument, replaceUpsertOption)
    }

    private fun today() = ofPattern("yyyy-MM-dd").format(now())
}
