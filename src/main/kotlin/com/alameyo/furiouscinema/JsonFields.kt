package com.alameyo.furiouscinema

enum class JsonFields(val fieldName: String) {
    TIME_TABLES("timeTables"),
    TIME_SLOTS("timeSlots"),
    DATE("date"),
    START_HOUR("startHour"),
    END_HOUR("endHour"),
    ROOM("room"),
    MOVIE_ID("movieId"),
    PRICE("price"),
    RATE("rate"),
    COMMENT("comment"),
    REVIEWS("reviews")
}
