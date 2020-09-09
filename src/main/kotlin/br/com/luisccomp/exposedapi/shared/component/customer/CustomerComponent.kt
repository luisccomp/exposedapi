package br.com.luisccomp.exposedapi.shared.component.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse
import org.springframework.stereotype.Component

@Component
class CustomerComponent {

    fun toCustomerResponse(customer: Customer): CustomerResponse {
        val contacts = ArrayList<ContactResponse>()

        customer.contacts
                .forEach {
                    contacts.add(ContactResponse(it.id.value,
                            it.name,
                            it.email,
                            it.phone))
                }

        return CustomerResponse(customer.firstName,
                customer.lastName,
                customer.email,
                customer.phone,
                contacts)
    }

}