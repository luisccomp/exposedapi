package br.com.luisccomp.exposedapi.domain.core.model.response.customer

import br.com.luisccomp.exposedapi.domain.core.model.response.contact.ContactResponse
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class CustomerResponse(
        val firstName: String,
        val lastName: String,
        val email: String,
        val phone: String,
        val contacts: List<ContactResponse>? = null
)