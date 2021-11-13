package com.alameyo.furiouscinema.inputvalidation

import com.alameyo.furiouscinema.toDate
import java.time.format.DateTimeParseException
import org.springframework.stereotype.Component

@Component
class DateValidator : FuriousValidator {
    override fun validate(value: String) {
        try {
            value.toDate()
        } catch (exception: DateTimeParseException) {
            throw InputValidationException()
        }
    }
}