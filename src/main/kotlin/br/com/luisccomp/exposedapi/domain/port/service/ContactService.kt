package br.com.luisccomp.exposedapi.domain.port.service

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.ContactCreateRequest
import java.util.UUID

interface ContactService {

    fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): Long

}
