package co.edu.iub.myfirstproject.repository

import co.edu.iub.myfirstproject.model.Notificacion
import org.springframework.data.jpa.repository.JpaRepository

interface NotificacionRepository : JpaRepository<Notificacion, Long>
