package br.com.luisccomp.exposedapi.domain.port.rest

import br.com.luisccomp.exposedapi.shared.exception.BadRequestException
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.lang.RuntimeException

interface RestExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(runtimeException: RuntimeException, webRequest: WebRequest): ResponseEntity<Any>

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(runtimeException: RuntimeException, webRequest: WebRequest): ResponseEntity<Any>

}