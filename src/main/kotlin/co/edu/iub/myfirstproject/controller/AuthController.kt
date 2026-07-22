package co.edu.iub.myfirstproject.controller

import co.edu.iub.myfirstproject.dto.RegisterRequest
import co.edu.iub.myfirstproject.dto.UserResponse
import co.edu.iub.myfirstproject.dto.auth.LoginRequest
import co.edu.iub.myfirstproject.dto.auth.TokenResponse
import co.edu.iub.myfirstproject.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/auth")
class AuthController(
    val authService: AuthService
) {

    @PostMapping("/register")
    fun register (@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.login(request))
    }
}