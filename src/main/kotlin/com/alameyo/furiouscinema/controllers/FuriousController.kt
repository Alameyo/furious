package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.inputvalidation.FuriousValidator
import com.alameyo.furiouscinema.inputvalidation.InputValidationException

interface FuriousController {
    fun validateInput(body: String, validator: FuriousValidator): Boolean {
        try {
            validator.validate(body)
        } catch (exception: InputValidationException) {
            println("Log: $exception")
            return true
        }
        return false
    }
}
