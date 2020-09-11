package br.com.luisccomp.exposedapi.adapter.rest.contact

import br.com.luisccomp.exposedapi.adapter.service.contact.ContactServiceTest.Companion.createContactCreateRequest
import br.com.luisccomp.exposedapi.domain.core.constant.Schema
import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactPublicControllerTest {

    lateinit var uuid: UUID

    @Autowired
    lateinit var mvc: MockMvc

    val objectMapper = ObjectMapper()

    companion object {
        const val CONTACT_API = "/public/api/v1/customers/%s/contacts"
    }

    @BeforeAll
    fun setUp() {
        Database.connect(DataSourceBuilder.create()
                .url("jdbc:h2:mem:test")
                .driverClassName("org.h2.Driver")
                .username("sa")
                .password("")
                .build())

        transaction {
            SchemaUtils.createSchema(org.jetbrains.exposed.sql.Schema(Schema.SCHEMA_CUSTOMER))
            SchemaUtils.createSchema(org.jetbrains.exposed.sql.Schema(Schema.SCHEMA_CONTACT))
            SchemaUtils.create(CustomerTable)
            SchemaUtils.create(ContactTable)

            uuid = Customer.new {
                firstName = "FName"
                lastName = "LName"
                email = "customer_new@email.com"
                phone = "99966-8485"
            }.id.value
        }
    }

    @BeforeEach
    fun clearDb() {
        transaction {
            ContactTable.deleteAll()
        }
    }

    @Test
    @DisplayName("Should register a contact on database")
    fun `register contact for a customer test`() {
        val contactCreateRequest = createContactCreateRequest()

        val json = objectMapper.writeValueAsString(contactCreateRequest)

        val request = post(String.format(CONTACT_API, uuid.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        val id = mvc.perform(request)
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString

        val contact = transaction {
            Contact.findById(id.toLong())
        }

        assertThat(contact).isNotNull
    }

}