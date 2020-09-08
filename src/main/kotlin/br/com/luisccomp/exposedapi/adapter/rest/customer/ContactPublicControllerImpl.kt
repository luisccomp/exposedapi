package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.rest.customer.ContactPublicController
import br.com.luisccomp.exposedapi.domain.port.service.ContactService
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*

@RestController
class ContactPublicControllerImpl(private val customerService: CustomerService,
                                  private val contactService: ContactService) : ContactPublicController {

    override fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): ResponseEntity<Any> {
        val id = contactService.register(uuid, contactCreateRequest)

        return ResponseEntity
                .created(URI("/$CUSTOMER_PUBLIC_RESOURCE/${uuid}/contacts/${id}"))
                .build()
    }

}