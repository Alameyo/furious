package com.alameyo.furiouscinema

import com.alameyo.furiouscinema.inputvalidation.InputValidationException
import com.google.gson.JsonParser.parseString
import java.time.LocalTime

fun String.asJsonObject() = parseString(this).asJsonObject!!
fun String.toTime() = LocalTime.parse(this) ?: throw InputValidationException()
