package co.edu.iub.myfirstproject.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "notificaciones")
class Notificacion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recordatorio_id", nullable = false)
    var recordatorio: Recordatorio,

    @Column(nullable = false)
    var exitoso: Boolean,

    var mensajeError: String? = null,

    @Column(nullable = false)
    var enviadoEn: LocalDateTime = LocalDateTime.now()
)
