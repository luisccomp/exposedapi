package br.com.luisccomp.exposedapi.adapter.service.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.ContactService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContactServiceImpl : ContactService {

    override fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): Long {
        TODO("Not yet implemented")
    }

}