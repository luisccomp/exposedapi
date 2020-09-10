package br.com.luisccomp.exposedapi.domain.port.service.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import org.springframework.data.domain.Pageable
import java.util.UUID

interface CustomerService {

    fun delete(uuid: UUID)

    fun existsById(uuid: UUID): Boolean

    fun findAll(pageable: Pageable): List<Customer>

    fun findById(uuid: UUID): Customer

    fun register(customerCreateRequest: CustomerCreateRequest): UUID

    fun update(customerCreateRequest: CustomerCreateRequest, uuid: UUID): Customer

}
