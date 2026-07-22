package co.edu.iub.myfirstproject.dto.auth

data class TokenResponse (
    val access_token: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long

)