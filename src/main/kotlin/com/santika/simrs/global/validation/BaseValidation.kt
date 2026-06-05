package com.santika.simrs.global.validation

import com.santika.simrs.global.exception.FieldValidationException
import com.santika.simrs.global.utils.toSnakeCase

import jakarta.validation.Validator
import org.springframework.stereotype.Component

@Component
class BaseValidation(private val validator: Validator) {
    fun <T> validate(request: T, customValidation: (MutableMap<String, String>) -> Unit): MutableMap<String, String> {
        val errors = mutableMapOf<String, String>()

        validator.validate(request).forEach { error ->
            val fieldName = error.propertyPath.toString().toSnakeCase()
            errors[fieldName] = error.message
        }

        customValidation(errors)

        if (errors.isNotEmpty()) throw FieldValidationException(errors)

        return errors
    }

    companion object
}