package br.com.luisccomp.exposedapi.adapter.service.contact

import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.contact.ContactService
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class ContactServiceImpl : ContactService {

    override fun findAll(uuid: UUID): List<Contact> {
        val result = ArrayList<Contact>()

        transaction {
            Contact.find { ContactTable.customerId eq uuid }
                    .forEach { result.add(it) }
        }

        return result
    }

    override fun findAll(uuid: UUID, pageable: Pageable): List<Contact> {
        val contacts = arrayListOf<Contact>()

        transaction {
            Contact.find { ContactTable.customerId eq uuid }
                    .forEach {
                        contacts.add(it)
                    }
        }

        return contacts
    }

    override fun findById(uuid: UUID, id: Long): Contact? {
        return transaction {
            addLogger(StdOutSqlLogger)

            Contact.find {
                ContactTable.id.eq(id) and ContactTable.customerId.eq(uuid)
            }.toList().firstOrNull()
        }
    }

    override fun register(uuid: UUID, contactCreateRequest: ContactCreateRequest): Long {
        val customerFound = transaction {
            Customer.findById(uuid)
        } ?: throw NotFoundException("Customer not found")

        return transaction {
            Contact.new {
                name = contactCreateRequest.name
                email = contactCreateRequest.email
                phone = contactCreateRequest.phone
                customer = customerFound
            }
        } .id.value
    }

}