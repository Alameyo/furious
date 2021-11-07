package com.alameyo.furiouscinema

import com.google.gson.JsonParser.parseString

fun String.asJsonObject() = parseString(this).asJsonObject!!
