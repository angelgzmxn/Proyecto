package co.edu.iub.myfirstproject.service

import co.edu.iub.myfirstproject.model.EstadoRecordatorio
import co.edu.iub.myfirstproject.model.Notificacion
import co.edu.iub.myfirstproject.repository.NotificacionRepository
import co.edu.iub.myfirstproject.repository.RecordatorioRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RecordatorioSchedulerService(
    private val recordatorioRepository: RecordatorioRepository,
    private val notificacionRepository: NotificacionRepository,
    private val whatsAppService: WhatsAppService
) {

    private val logger = LoggerFactory.getLogger(RecordatorioSchedulerService::class.java)

    @Scheduled(fixedRateString = "\${app.scheduler.fixed-rate-ms:60000}")
    fun enviarRecordatoriosPendientes() {
        val pendientes = recordatorioRepository.findAllByEstadoAndFechaRecordatorioLessThanEqual(
            EstadoRecordatorio.PENDIENTE,
            LocalDateTime.now()
        )

        pendientes.forEach { recordatorio ->
            val numero = recordatorio.owner.whatsappNumber

            if (numero.isNullOrBlank()) {
                logger.warn("Recordatorio ${recordatorio.id} sin numero de WhatsApp configurado, se omite")
                return@forEach
            }

            val mensaje = "Recordatorio: ${recordatorio.titulo}" +
                (recordatorio.descripcion?.let { " - $it" } ?: "")

            val exitoso = whatsAppService.sendReminder(numero, mensaje)

            notificacionRepository.save(
                Notificacion(
                    recordatorio = recordatorio,
                    exitoso = exitoso,
                    mensajeError = if (exitoso) null else "Fallo el envio via Twilio"
                )
            )

            if (exitoso) {
                recordatorio.estado = EstadoRecordatorio.NOTIFICADO
                recordatorio.updatedAt = LocalDateTime.now()
                recordatorioRepository.save(recordatorio)
            }
        }
    }
}
