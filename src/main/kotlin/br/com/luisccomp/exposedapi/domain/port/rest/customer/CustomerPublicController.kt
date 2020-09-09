package br.com.luisccomp.exposedapi.domain.port.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE_REGISTER
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(CUSTOMER_PUBLIC_RESOURCE)
interface CustomerPublicController {

    @PostMapping
    fun register(customerCreateRequest: CustomerCreateRequest): ResponseEntity<Any>

}