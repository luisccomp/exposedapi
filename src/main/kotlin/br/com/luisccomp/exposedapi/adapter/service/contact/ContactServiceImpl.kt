package br.com.luisccomp.exposedapi.adapter.service.contact

import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.contact.ContactService
import br.com.luisccomp.exposedapi.shared.exception.BadRequestException
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class ContactServiceImpl : ContactService {

    override fun delete(uuid: UUID, id: Long) {
        transaction {
            Customer.findById(uuid)?: throw BadRequestException("Customer not found")

            val contact = Contact.find { (ContactTable.id eq id) and (ContactTable.customerId eq uuid) }
                    .firstOrNull()?: throw NotFoundException("Contact not found")

            contact.delete()
        }
    }

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
        return transaction {
            val customerFound = (Customer.findById(uuid) ?: throw BadRequestException("Customer not found"))

            Contact.new {
                name = contactCreateRequest.name
                email = contactCreateRequest.email
                phone = contactCreateRequest.phone
                customer = customerFound
            }
        } .id.value
    }

    override fun update(uuid: UUID, contactCreateRequest: ContactCreateRequest, id: Long): Contact {
        transaction { Customer.findById(uuid) }?: throw BadRequestException("Customer not found")

        val contact = transaction {
            Contact.find { ContactTable.id.eq(id) and ContactTable.customerId.eq(uuid) }
                    .toList()
                    .firstOrNull()
        }?: throw NotFoundException("Contact not found")

        return transaction {
            contact.name = contactCreateRequest.name
            contact.email = contactCreateRequest.email
            contact.phone = contactCreateRequest.phone

            contact
        }
    }

}
