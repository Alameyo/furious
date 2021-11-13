package com.alameyo.furiouscinema.inputvalidation

import org.springframework.stereotype.Component

@Component
class MovieIdValidator: FuriousValidator {
    private val movieIdLength = 9

    override fun validate(value: String) {
        if (value.length != movieIdLength) throw InputValidationException()
    }
}
