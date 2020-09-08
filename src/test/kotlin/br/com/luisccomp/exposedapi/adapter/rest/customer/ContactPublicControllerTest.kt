package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.request.customer.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema
import br.com.luisccomp.exposedapi.domain.port.service.ContactService
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.times
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest
class ContactPublicControllerTest {

    @MockBean
    private lateinit var contactService: ContactService

    @MockBean
    private lateinit var customerService: CustomerService

    @Autowired
    private lateinit var mvc: MockMvc

    private val objectMapper: ObjectMapper = ObjectMapper()

    companion object {
        const val CONTACT_API = "/${ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE}/%s/contacts"

        fun createContactCreateRequest(): ContactCreateRequest =
                createContactCreateRequest("ContactName", "contact_email@provider.com", "99999-1111")

        fun createContactCreateRequest(name: String, email: String, phone: String): ContactCreateRequest =
                ContactCreateRequest(name, email, phone)
    }

    fun <T> any(): T = Mockito.any()

    inline fun <reified T> any(cls: Class<T>): T = Mockito.any(T::class.java)

    @Test
    @DisplayName("Should register a contact on repository for a customer")
    fun `register contact for customer test`() {
        val contactCreateRequest = createContactCreateRequest()

        val uuid = UUID.randomUUID()

        val json = objectMapper.writeValueAsString(contactCreateRequest)

        given(contactService.register(any(UUID::class.java), any(ContactCreateRequest::class.java)))
                .willReturn(1L)

        val request = post(String.format(CONTACT_API, uuid.toString()) + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isCreated)

        verify(contactService, times(1))
                .register(any(UUID::class.java), any(ContactCreateRequest::class.java))
    }

}