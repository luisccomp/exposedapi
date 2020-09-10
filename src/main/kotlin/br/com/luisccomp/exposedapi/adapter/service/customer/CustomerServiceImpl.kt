package br.com.luisccomp.exposedapi.adapter.service.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import br.com.luisccomp.exposedapi.shared.exception.BadRequestException
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerServiceImpl : CustomerService {

    override fun delete(uuid: UUID) {
        val customer = (transaction {
            Customer.findById(uuid)
        } ?: throw NotFoundException("Customer not found"))

        transaction {
            customer.delete()
        }
    }

    override fun existsById(uuid: UUID): Boolean {
        return transaction {
            Customer.find { CustomerTable.id eq uuid }
                    .count()
        } > 0
    }

    override fun findAll(pageable: Pageable): List<Customer> {
        return transaction {
            Customer.all()
                    .limit(pageable.pageSize, pageable.offset)
                    .toList()
        }
    }

    override fun findById(uuid: UUID): Customer {
        return (transaction {
            addLogger(StdOutSqlLogger)
            Customer.findById(uuid)
        }?: throw NotFoundException("Customer not found"))
    }

    override fun register(customerCreateRequest: CustomerCreateRequest): UUID {
        val customer = transaction {
            Customer.find { CustomerTable.email eq customerCreateRequest.email }
                    .firstOrNull()?.let { throw BadRequestException("Email already in use") }

            Customer.new {
                firstName = customerCreateRequest.firstName
                lastName = customerCreateRequest.lastName
                email = customerCreateRequest.email
                phone = customerCreateRequest.phone
            }
        }

        return customer.id.value
    }

    override fun update(customerCreateRequest: CustomerCreateRequest, uuid: UUID): Customer {
        return transaction {
            val customer = (Customer.findById(uuid)?: throw NotFoundException("Customer not found")) as Customer

            customer.firstName = customerCreateRequest.firstName
            customer.lastName = customerCreateRequest.lastName
            customer.email = customerCreateRequest.email
            customer.phone = customerCreateRequest.phone

            customer
        }
    }

}