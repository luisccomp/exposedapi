package br.com.luisccomp.exposedapi.domain.core.model.entity.contact

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Contact(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<Contact> (ContactTable)

    var name by ContactTable.name

    var email by ContactTable.email

    var phone by ContactTable.phone
    
    var customer by Customer referencedOn ContactTable.customerId

}