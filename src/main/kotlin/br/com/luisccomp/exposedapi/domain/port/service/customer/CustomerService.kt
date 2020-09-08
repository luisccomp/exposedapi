package br.com.luisccomp.exposedapi.domain.port.service.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import java.util.UUID

interface CustomerService {

    fun register(customerCreateRequest: CustomerCreateRequest): UUID

}
