package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE_REGISTER
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest
class CustomerPublicControllerTest {

    private val objectMapper = ObjectMapper()

    @MockBean
    lateinit var customerService: CustomerService

    @Autowired
    lateinit var mvc: MockMvc

    companion object {
        fun createCustomerCreateRequest(): CustomerCreateRequest =
                CustomerCreateRequest("CustomerName",
                        "CustomerLastName",
                        "customer_email@provider.com",
                        "99999-1234")
    }

    private fun <T> any(): T = Mockito.any<T>()

    @Test
    @DisplayName("Should create a customer, store it on repository and return it's UUID")
    fun `create customer test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        val json = objectMapper.writeValueAsString(customerCreateRequest)

        given(customerService.register(any()))
                .willReturn(UUID.randomUUID())

        val request = post("$CUSTOMER_PUBLIC_RESOURCE$CUSTOMER_PUBLIC_RESOURCE_REGISTER")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isCreated)

        verify(customerService, times(1)).register(any())
    }

}