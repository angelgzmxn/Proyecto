package co.edu.iub.myfirstproject.service

import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioCreateRequest
import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioResponse
import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioUpdateRequest
import co.edu.iub.myfirstproject.exception.InvalidCredentialsException
import co.edu.iub.myfirstproject.exception.InvalidStatusTransitionException
import co.edu.iub.myfirstproject.exception.ResourceNotFoundException
import co.edu.iub.myfirstproject.model.EstadoRecordatorio
import co.edu.iub.myfirstproject.model.Recordatorio
import co.edu.iub.myfirstproject.repository.RecordatorioRepository
import co.edu.iub.myfirstproject.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RecordatorioService(
    private val recordatorioRepository: RecordatorioRepository,
    private val userRepository: UserRepository
) {

    fun createRecordatorio(ownerEmail: String, request: RecordatorioCreateRequest): RecordatorioResponse {
        val owner = findUserByEmail(ownerEmail)
        val recordatorio = Recordatorio(
            titulo = request.titulo,
            descripcion = request.descripcion,
            fechaRecordatorio = request.fechaRecordatorio,
            owner = owner
        )
        return recordatorioRepository.save(recordatorio).toResponse()
    }

    fun getRecordatorios(ownerEmail: String): List<RecordatorioResponse> {
        val owner = findUserByEmail(ownerEmail)
        return recordatorioRepository.findAllByOwnerId(requireNotNull(owner.id)).map { it.toResponse() }
    }

    fun getRecordatorio(id: Long, ownerEmail: String): RecordatorioResponse {
        val owner = findUserByEmail(ownerEmail)
        val recordatorio = recordatorioRepository.findByIdAndOwnerId(id, requireNotNull(owner.id))
            ?: throw ResourceNotFoundException("Recordatorio not found")
        return recordatorio.toResponse()
    }

    fun updateRecordatorio(id: Long, ownerEmail: String, request: RecordatorioUpdateRequest): RecordatorioResponse {
        val owner = findUserByEmail(ownerEmail)
        val recordatorio = recordatorioRepository.findByIdAndOwnerId(id, requireNotNull(owner.id))
            ?: throw ResourceNotFoundException("Recordatorio not found")

        request.estado?.let { validateEstadoTransition(recordatorio.estado, it) }

        request.titulo?.let { recordatorio.titulo = it }
        recordatorio.descripcion = request.descripcion ?: recordatorio.descripcion
        request.fechaRecordatorio?.let { recordatorio.fechaRecordatorio = it }
        request.estado?.let { recordatorio.estado = it }
        recordatorio.updatedAt = LocalDateTime.now()

        return recordatorioRepository.save(recordatorio).toResponse()
    }

    fun deleteRecordatorio(id: Long, ownerEmail: String) {
        val owner = findUserByEmail(ownerEmail)
        val recordatorio = recordatorioRepository.findByIdAndOwnerId(id, requireNotNull(owner.id))
            ?: throw ResourceNotFoundException("Recordatorio not found")
        recordatorioRepository.delete(recordatorio)
    }

    fun getAllRecordatorios(): List<RecordatorioResponse> {
        return recordatorioRepository.findAll().map { it.toResponse() }
    }

    private fun findUserByEmail(email: String) =
        userRepository.findByEmail(email)
            ?: throw InvalidCredentialsException("User not found")

    // PENDIENTE -> NOTIFICADO (la marcara el modulo de notificaciones mas adelante)
    // PENDIENTE -> COMPLETADO (el usuario lo marca como hecho sin esperar la notificacion)
    // NOTIFICADO -> COMPLETADO
    // VENCIDO solo lo pondra el scheduler cuando exista; el usuario no lo puede fijar a mano
    private fun validateEstadoTransition(current: EstadoRecordatorio, next: EstadoRecordatorio) {
        if (current == next) return

        val allowed = when (current) {
            EstadoRecordatorio.PENDIENTE -> next == EstadoRecordatorio.NOTIFICADO || next == EstadoRecordatorio.COMPLETADO
            EstadoRecordatorio.NOTIFICADO -> next == EstadoRecordatorio.COMPLETADO
            EstadoRecordatorio.COMPLETADO -> false
            EstadoRecordatorio.VENCIDO -> false
        }

        if (!allowed) {
            throw InvalidStatusTransitionException("Invalid estado transition")
        }
    }

    private fun Recordatorio.toResponse(): RecordatorioResponse {
        return RecordatorioResponse(
            id = requireNotNull(id),
            titulo = titulo,
            descripcion = descripcion,
            fechaRecordatorio = fechaRecordatorio,
            estado = estado,
            userId = requireNotNull(owner.id),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}
