package br.com.luisccomp.exposedapi.domain.port.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RequestMapping(CUSTOMER_PUBLIC_RESOURCE)
interface CustomerPublicController {

    @GetMapping
    fun findAll(pageable: Pageable): Page<CustomerResponse>

    @GetMapping("/{uuid}")
    fun findById(@PathVariable uuid: UUID): CustomerResponse

    @PostMapping
    fun register(@RequestBody @Valid customerCreateRequest: CustomerCreateRequest): ResponseEntity<Any>

    @PutMapping("/{uuid}")
    fun update(@PathVariable uuid: UUID,
               @RequestBody @Valid customerCreateRequest: CustomerCreateRequest): CustomerResponse

    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable uuid: UUID)

}