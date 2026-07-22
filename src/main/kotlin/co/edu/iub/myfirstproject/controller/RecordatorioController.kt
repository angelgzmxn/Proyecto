package co.edu.iub.myfirstproject.controller

import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioCreateRequest
import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioResponse
import co.edu.iub.myfirstproject.dto.recordatorio.RecordatorioUpdateRequest
import co.edu.iub.myfirstproject.service.RecordatorioService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recordatorios")
class RecordatorioController(
    private val recordatorioService: RecordatorioService
) {

    @PostMapping
    fun create(
        @Valid @RequestBody request: RecordatorioCreateRequest,
        authentication: Authentication
    ): ResponseEntity<RecordatorioResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(recordatorioService.createRecordatorio(authentication.name, request))
    }

    @GetMapping
    fun list(authentication: Authentication): List<RecordatorioResponse> {
        return recordatorioService.getRecordatorios(authentication.name)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long, authentication: Authentication): RecordatorioResponse {
        return recordatorioService.getRecordatorio(id, authentication.name)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: RecordatorioUpdateRequest,
        authentication: Authentication
    ): RecordatorioResponse {
        return recordatorioService.updateRecordatorio(id, authentication.name, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, authentication: Authentication): ResponseEntity<Void> {
        recordatorioService.deleteRecordatorio(id, authentication.name)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/all")
    fun listAll(): List<RecordatorioResponse> {
        return recordatorioService.getAllRecordatorios()
    }
}
