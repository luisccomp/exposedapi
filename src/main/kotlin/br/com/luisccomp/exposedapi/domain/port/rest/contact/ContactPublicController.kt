package br.com.luisccomp.exposedapi.domain.port.rest.contact

import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.ContactResources.CONTACT_PUBLIC_RESOURCE
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RequestMapping(CONTACT_PUBLIC_RESOURCE)
interface ContactPublicController {

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable uuid: UUID, @PathVariable id: Long)

    @GetMapping
    fun findAll(@PathVariable uuid: UUID, pageable: Pageable): Page<ContactResponse>

    @GetMapping("/{id}")
    fun findById(@PathVariable uuid: UUID, @PathVariable id: Long): ContactResponse

    @PostMapping
    fun register(@PathVariable uuid: UUID,
                 @RequestBody @Valid contactCreateRequest: ContactCreateRequest): ResponseEntity<Any>

    @PutMapping("/{id}")
    fun update(@PathVariable uuid: UUID,
               @PathVariable id: Long,
               @RequestBody @Valid contactCreateRequest: ContactCreateRequest): ContactResponse

}