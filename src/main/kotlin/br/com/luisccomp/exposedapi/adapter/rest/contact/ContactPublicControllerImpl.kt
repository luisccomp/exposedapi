package br.com.luisccomp.exposedapi.adapter.rest.contact

import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.rest.contact.ContactPublicController
import br.com.luisccomp.exposedapi.domain.port.service.contact.ContactService
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import br.com.luisccomp.exposedapi.shared.component.contact.ContactComponent
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*
import java.util.stream.Collectors

@RestController
class ContactPublicControllerImpl(private val customerService: CustomerService,
                                  private val contactService: ContactService,
                                  private val contactComponent: ContactComponent) : ContactPublicController {

    override fun findAll(uuid: UUID, pageable: Pageable): ResponseEntity<Page<ContactResponse>> {
        val result = transaction { contactService.findAll(uuid, pageable)
                .stream()
                .map { contactComponent.toContactResponse(it) }
                .collect(Collectors.toList()) }

        return ResponseEntity
                .ok(PageImpl<ContactResponse>(result,
                        PageRequest.of(pageable.pageNumber, pageable.pageSize),
                        result.size.toLong()))
    }

    override fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): ResponseEntity<Any> {
        val id = contactService.register(uuid, contactCreateRequest)

        return ResponseEntity
                .created(URI("/$CUSTOMER_PUBLIC_RESOURCE/${uuid}/contacts/${id}"))
                .build()
    }

}