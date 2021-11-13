package com.alameyo.furiouscinema.inputvalidation

import com.alameyo.furiouscinema.asJsonObject
import com.google.gson.JsonParseException
import org.springframework.stereotype.Component

@Component
class MovieReviewValidator : FuriousValidator {
    private val minRate = 1
    private val maxRate = 5
    private val maxCommentLength = 250

    override fun validate(value: String) {
        try {
            val review = value.asJsonObject()
            val rate = review["rate"].asInt
            val commentLength = review["comment"]?.asString?.length ?: 0

            when {
                rate > maxRate || rate < minRate -> throw InputValidationException()
                commentLength > maxCommentLength -> throw InputValidationException()
            }
        } catch (exception: JsonParseException) {
            throw InputValidationException()
        }
    }
}
