package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.repositories.TimeTableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TimeTableController {
    @Autowired
    lateinit var timeTableRepository: TimeTableRepository

    @GetMapping("/furious/timetable/{date}")
    fun getTimeTable(@PathVariable(value = "date") date: String) = ResponseEntity(timeTableRepository.getTimeTable(date), OK)

    @GetMapping("/furious/timetable")
    fun getTimeTable() = ResponseEntity(timeTableRepository.getTimeTable(), OK)

    @GetMapping("/furious/timetables")
    fun getTimeTables() = ResponseEntity(timeTableRepository.getTimeTables(), OK)

    @PutMapping("/furious/timetable")
    fun putTimeTable(@RequestBody body: String) {
        timeTableRepository.createOrReplaceTimeTable(body.asJsonObject())
    }
}
