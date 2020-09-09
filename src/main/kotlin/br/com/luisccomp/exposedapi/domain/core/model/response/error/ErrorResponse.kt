package br.com.luisccomp.exposedapi.domain.core.model.response.error

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
        val status: Int? = null,
        val message: String? = null,
        val errors: List<FieldError>? = null
) {

    data class FieldError(
            val fieldName: String? = null,
            val message: String? = null
    )

}
