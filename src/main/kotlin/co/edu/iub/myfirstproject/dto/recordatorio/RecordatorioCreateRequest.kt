package co.edu.iub.myfirstproject.dto.recordatorio

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class RecordatorioCreateRequest(
    @field:NotBlank
    val titulo: String,

    val descripcion: String?,

    @field:NotNull
    @field:Future
    val fechaRecordatorio: LocalDateTime
)
