package br.com.luisccomp.exposedapi.domain.core.model.entity.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import com.fasterxml.jackson.annotation.JsonBackReference
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class Customer(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<Customer> (CustomerTable)

    var firstName by CustomerTable.firstName

    var lastName by CustomerTable.lastName

    var email by CustomerTable.email

    var phone by CustomerTable.phone

    val contacts by Contact referrersOn ContactTable.customerId

}
