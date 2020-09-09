package br.com.luisccomp.exposedapi.adapter.rest

import br.com.luisccomp.exposedapi.domain.core.model.response.error.ErrorResponse
import br.com.luisccomp.exposedapi.domain.port.rest.RestExceptionHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.RuntimeException
import java.util.stream.Collectors

@RestControllerAdvice
class RestExceptionHandlerImpl : RestExceptionHandler, ResponseEntityExceptionHandler() {

    override fun handleNotFound(runtimeException: RuntimeException, webRequest: WebRequest): ResponseEntity<Any> =
            createResponse(runtimeException, HttpHeaders(), HttpStatus.NOT_FOUND, webRequest)

    override fun handleBadRequest(runtimeException: RuntimeException, webRequest: WebRequest): ResponseEntity<Any> =
            createResponse(runtimeException, HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest)

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                              headers: HttpHeaders,
                                              status: HttpStatus,
                                              request: WebRequest): ResponseEntity<Any> {
        val errors = ex.bindingResult
                .allErrors
                .stream()
                .map { ErrorResponse.FieldError((it as FieldError).field, it.defaultMessage) }
                .collect(Collectors.toList())

        val errorResponse = ErrorResponse(status.value(), ex.message, errors)

        return handleExceptionInternal(ex, errorResponse, headers, status, request)
    }

    private fun createResponse(runtimeException: RuntimeException,
                               headers: HttpHeaders,
                               status: HttpStatus,
                               request: WebRequest): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(status.value(), runtimeException.message)

        return handleExceptionInternal(runtimeException, errorResponse, headers, status, request)
    }

}