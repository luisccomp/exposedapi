package br.com.luisccomp.exposedapi.domain.core.model.entity.customer

import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
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

}
