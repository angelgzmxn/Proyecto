package co.edu.iub.myfirstproject.dto.user

import jakarta.validation.constraints.Email

data class UpdateProfileRequest(
    @field:Email
    val email: String?,

    val fullName: String?,

    val whatsappNumber: String?
)
