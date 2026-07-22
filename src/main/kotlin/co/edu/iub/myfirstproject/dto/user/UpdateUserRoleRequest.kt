package co.edu.iub.myfirstproject.dto.user

import co.edu.iub.myfirstproject.model.UserRole
import jakarta.validation.constraints.NotNull

data class UpdateUserRoleRequest(
    @field:NotNull
    val role: UserRole
)
