package br.com.luisccomp.exposedapi.domain.core.model.response.contact

import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse

data class ContactResponse(
        val id: Long,
        val name: String,
        val email: String,
        val phone: String,
        val customer: CustomerResponse? = null
)