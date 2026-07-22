package co.edu.iub.myfirstproject.config

import co.edu.iub.myfirstproject.model.User
import co.edu.iub.myfirstproject.model.UserRole
import co.edu.iub.myfirstproject.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        val adminEmail = "admin@recordatorios.com"

        if (!userRepository.existsByEmail(adminEmail)) {
            val admin = User(
                email = adminEmail,
                fullName = "System Administrator",
                password = passwordEncoder.encode("AdminSecret123")!!,
                role = UserRole.ADMIN
            )
            userRepository.save(admin)
        }
    }
}
