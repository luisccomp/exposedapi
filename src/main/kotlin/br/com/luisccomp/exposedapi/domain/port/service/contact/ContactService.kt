package br.com.luisccomp.exposedapi.domain.port.service.contact

import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ContactService {

    fun findAll(uuid: UUID): List<Contact>

    fun findAll(uuid: UUID, pageable: Pageable): List<Contact>

    fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): Long

}
