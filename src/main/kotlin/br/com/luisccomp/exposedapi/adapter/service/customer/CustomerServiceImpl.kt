package br.com.luisccomp.exposedapi.adapter.service.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerServiceImpl : CustomerService {

    override fun register(customerCreateRequest: CustomerCreateRequest): UUID {
        val customer = transaction {
            Customer.new {
                firstName = customerCreateRequest.firstName
                lastName = customerCreateRequest.lastName
                email = customerCreateRequest.email
                phone = customerCreateRequest.phone
            }
        }

        return customer.id.value
    }

}