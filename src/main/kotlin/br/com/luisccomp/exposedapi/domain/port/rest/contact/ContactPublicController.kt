package br.com.luisccomp.exposedapi.domain.port.rest.contact

import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.ContactResources.CONTACT_PUBLIC_RESOURCE
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RequestMapping(CONTACT_PUBLIC_RESOURCE)
interface ContactPublicController {

    @GetMapping
    fun findAll(@PathVariable uuid: UUID, pageable: Pageable): ResponseEntity<Page<ContactResponse>>

    @PostMapping
    fun register(@PathVariable uuid: UUID,
                 @RequestBody @Valid contactCreateRequest: ContactCreateRequest): ResponseEntity<Any>

}