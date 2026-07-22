package co.edu.iub.myfirstproject.dto.recordatorio

import co.edu.iub.myfirstproject.model.EstadoRecordatorio
import java.time.LocalDateTime

data class RecordatorioResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String?,
    val fechaRecordatorio: LocalDateTime,
    val estado: EstadoRecordatorio,
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
