package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.rest.customer.CustomerPublicController
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@RestController
class CustomerPublicControllerImpl(private val customerService: CustomerService) : CustomerPublicController {

    override fun register(@RequestBody @Valid customerCreateRequest: CustomerCreateRequest): ResponseEntity<Any> {
        val id = customerService.register(customerCreateRequest)

        return ResponseEntity
                .created(URI("$CUSTOMER_PUBLIC_RESOURCE${id}"))
                .build()
    }

}