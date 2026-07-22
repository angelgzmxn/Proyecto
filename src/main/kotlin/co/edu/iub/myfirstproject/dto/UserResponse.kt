package co.edu.iub.myfirstproject.dto

import co.edu.iub.myfirstproject.model.UserRole
import java.time.LocalDateTime

data class UserResponse (
    val id: Long,
    val email: String,
    val fullName: String,
    val active: Boolean,
    val whatsappNumber: String?,
    val role: UserRole,
    val createdAt: LocalDateTime
)