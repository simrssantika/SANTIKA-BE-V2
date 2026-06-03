package com.santika.simrs.global.exception

class FieldValidationException(val errors: MutableMap<String, String>) :
    RuntimeException("Validation Failed ${errors.size} errors(s)")
