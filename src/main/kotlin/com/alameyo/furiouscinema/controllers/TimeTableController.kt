package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.inputvalidation.DateValidator
import com.alameyo.furiouscinema.inputvalidation.TimeTableValidator
import com.alameyo.furiouscinema.repositories.TimeTableRepository
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeTableController : FuriousController {
    @Autowired
    lateinit var timeTableRepository: TimeTableRepository

    @Autowired
    lateinit var timeTableValidator: TimeTableValidator

    @Autowired
    lateinit var dateValidator: DateValidator

    @GetMapping("/furious/timetable/{date}")
    fun getTimeTable(@PathVariable(value = "date") date: String): ResponseEntity<Document> {
        return when {
            validateInput(date, dateValidator) -> ResponseEntity(BAD_REQUEST)
            else -> ResponseEntity(timeTableRepository.getTimeTable(date), OK)
        }
    }

    @GetMapping("/furious/timetable")
    fun getTimeTable() = ResponseEntity(timeTableRepository.getTimeTable(), OK)

    @PutMapping("/furious/timetable")
    fun putTimeTable(@RequestBody body: String): HttpStatus {
        if (validateInput(body, timeTableValidator)) return BAD_REQUEST
        val timeTableDocument = timeTableRepository.createTimeTableDocument(body.asJsonObject())
        return when (timeTableRepository.commitTimeTable(timeTableDocument)) {
            true -> CREATED
            false -> OK
        }
    }
}
