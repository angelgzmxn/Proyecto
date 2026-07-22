package co.edu.iub.myfirstproject.dto.recordatorio

import co.edu.iub.myfirstproject.model.EstadoRecordatorio
import java.time.LocalDateTime

data class RecordatorioUpdateRequest(
    val titulo: String?,
    val descripcion: String?,
    val fechaRecordatorio: LocalDateTime?,
    val estado: EstadoRecordatorio?
)
