package br.com.luisccomp.exposedapi.domain.core.model.request.customer

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class ContactCreateRequest(
        @NotEmpty
        val name: String,

        @NotEmpty
        @Email
        val email: String,

        @NotEmpty
        val phone: String
)