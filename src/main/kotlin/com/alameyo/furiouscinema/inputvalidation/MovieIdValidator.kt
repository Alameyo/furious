package com.alameyo.furiouscinema.inputvalidation

class MovieIdValidator: FuriousValidator {

    private val movieIdLength = 9
    override fun validate(value: Any) {
        when {
            value !is String -> throw InputValidationException()
            value.length != movieIdLength -> throw InputValidationException()
        }
    }
}
