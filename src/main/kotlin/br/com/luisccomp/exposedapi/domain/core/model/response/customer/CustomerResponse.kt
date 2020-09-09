package br.com.luisccomp.exposedapi.domain.core.model.response.customer

import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse

data class CustomerResponse(
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val contacts: List<ContactResponse>? = null
)