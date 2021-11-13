package com.alameyo.furiouscinema

import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import com.google.gson.JsonParser.parseString
import java.time.LocalDate
import java.time.LocalTime

fun String.asJsonObject() = parseString(this).asJsonObject!!
fun String.asJsonArray() = parseString(this).asJsonArray!!
fun String.toTime() = LocalTime.parse(this) ?: throw InputValidationException()
fun String.toDate() = LocalDate.parse(this) ?: throw InputValidationException()
