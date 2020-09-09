package br.com.luisccomp.exposedapi.domain.core.model.response.contact

import br.com.luisccomp.exposedapi.domain.core.model.response.customer.CustomerResponse
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class ContactResponse(
        val id: Long,
        val name: String,
        val email: String,
        val phone: String,
        val customer: CustomerResponse? = null
)