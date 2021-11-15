package com.alameyo.furiouscinema.inputvalidation

import com.alameyo.furiouscinema.JsonFields.COMMENT
import com.alameyo.furiouscinema.JsonFields.RATE
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
            val rate = review[RATE.fieldName].asInt
            val commentLength = review[COMMENT.fieldName]?.asString?.length ?: 0

            when {
                rate > maxRate || rate < minRate -> throw InputValidationException()
                commentLength > maxCommentLength -> throw InputValidationException()
            }
        } catch (exception: JsonParseException) {
            throw InputValidationException()
        }
    }
}
