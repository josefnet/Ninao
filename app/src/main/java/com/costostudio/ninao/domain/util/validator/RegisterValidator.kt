package com.costostudio.ninao.domain.util.validator

object RegisterValidator {
    fun validate(firstName: String, lastName: String, email: String, password: String): Result<Unit> {
        return if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            Result.failure(Exception("Veuillez remplir tous les champs"))
        } else {
            Result.success(Unit)
        }
    }
}