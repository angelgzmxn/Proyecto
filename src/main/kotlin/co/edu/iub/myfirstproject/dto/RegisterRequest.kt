package co.edu.iub.myfirstproject.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest (
    @field: Email
    @field: NotBlank
    val email: String,

    @field: NotBlank
    val fullName: String,

    @field: NotBlank
    @field:Size(min = 8)
    val password: String
)