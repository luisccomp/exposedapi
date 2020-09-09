package br.com.luisccomp.exposedapi.shared.component.contact

import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse
import org.springframework.stereotype.Component

@Component
class ContactComponent {

    fun toContactResponse(contact: Contact): ContactResponse =
            ContactResponse(contact.id.value,
                    contact.name,
                    contact.email,
                    contact.phone,
                    CustomerResponse(contact.customer.firstName,
                            contact.customer.lastName,
                            contact.customer.email,
                            contact.customer.phone))

}