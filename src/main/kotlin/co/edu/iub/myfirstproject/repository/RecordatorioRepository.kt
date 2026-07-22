package co.edu.iub.myfirstproject.repository

import co.edu.iub.myfirstproject.model.EstadoRecordatorio
import co.edu.iub.myfirstproject.model.Recordatorio
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface RecordatorioRepository : JpaRepository<Recordatorio, Long> {
    fun findAllByOwnerId(ownerId: Long): List<Recordatorio>
    fun findByIdAndOwnerId(id: Long, ownerId: Long): Recordatorio?
    fun findAllByEstadoAndFechaRecordatorioLessThanEqual(
        estado: EstadoRecordatorio,
        fecha: LocalDateTime
    ): List<Recordatorio>
}
