package co.edu.iub.myfirstproject.service

import co.edu.iub.myfirstproject.dto.RegisterRequest
import co.edu.iub.myfirstproject.dto.UserResponse
import co.edu.iub.myfirstproject.dto.auth.LoginRequest
import co.edu.iub.myfirstproject.dto.auth.TokenResponse
import co.edu.iub.myfirstproject.exception.DuplicateResourceException
import co.edu.iub.myfirstproject.exception.InvalidCredentialsException
import co.edu.iub.myfirstproject.model.User
import co.edu.iub.myfirstproject.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun register(request: RegisterRequest) : UserResponse {
        val email = request.email.trim().lowercase()

        if (userRepository.existsByEmail(email)) {
            throw DuplicateResourceException("Email already exists")
        }

        val user = User(
            email = email,
            fullName = request.fullName.trim(),
            password = passwordEncoder.encode(request.password)!!
        )
        return userRepository.save(user).toResponse()
    }

    private fun User.toResponse() : UserResponse {
        return UserResponse(
            id = requireNotNull(id),
            email = email,
            fullName = fullName,
            active = active,
            whatsappNumber = whatsappNumber,
            role = requireNotNull(role),
            createdAt = createdAt
        )
    }

    fun login(request: LoginRequest): TokenResponse {
        val email = request.email.trim().lowercase()
        val user = userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException("Invalid credentials")

        if (!user.active || !passwordEncoder.matches(request.password, user.password)) {
            throw InvalidCredentialsException("Invalid credentials")
        }

        val token = jwtService.generateToken(user)
        return TokenResponse(
            access_token = token,
            expiresIn = jwtService.expirationMinutes * 60
        )
    }
}