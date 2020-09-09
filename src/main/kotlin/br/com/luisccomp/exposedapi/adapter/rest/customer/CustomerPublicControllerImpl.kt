package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.rest.customer.CustomerPublicController
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import br.com.luisccomp.exposedapi.shared.component.customer.CustomerComponent
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
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
class CustomerPublicControllerImpl(private val customerService: CustomerService,
                                   private val customerComponent: CustomerComponent) : CustomerPublicController {

    override fun findAll(pageable: Pageable): Page<CustomerResponse> {
        var result = transaction {
            customerService.findAll(pageable)
                    .stream()
                    .map { customerComponent.toCustomerResponse(it) }
                    .collect(Collectors.toList())
        }

        return PageImpl<CustomerResponse>(result,
                PageRequest.of(pageable.pageNumber, pageable.pageSize),
                result.size.toLong())
    }

    override fun findById(uuid: UUID): CustomerResponse {
        val customer = customerService.findById(uuid)?: NotFoundException("Customer not found")

        return transaction { customerComponent.toCustomerResponse(customer as Customer) }
    }

    override fun register(customerCreateRequest: CustomerCreateRequest): ResponseEntity<Any> {
        val id = customerService.register(customerCreateRequest)

        return ResponseEntity
                .created(URI("$CUSTOMER_PUBLIC_RESOURCE${id}"))
                .build()
    }

    override fun update(uuid: UUID, customerCreateRequest: CustomerCreateRequest): CustomerResponse {
        return transaction { customerComponent.toCustomerResponse(customerService.update(customerCreateRequest, uuid)) }
    }

    override fun delete(uuid: UUID) {
        customerService.delete(uuid)
    }

}