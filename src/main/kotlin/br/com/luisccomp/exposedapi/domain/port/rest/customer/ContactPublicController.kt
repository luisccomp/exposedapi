package br.com.luisccomp.exposedapi.domain.port.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.ContactResources.CONTACT_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.ContactResources.CONTACT_PUBLIC_RESOURCE_REGISTER
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID
import javax.validation.Valid

@RequestMapping(CONTACT_PUBLIC_RESOURCE)
interface ContactPublicController {

    @PostMapping(CONTACT_PUBLIC_RESOURCE_REGISTER)
    fun register(@PathVariable uuid: UUID,
                 @RequestBody @Valid contactCreateRequest: ContactCreateRequest): ResponseEntity<Any>

}