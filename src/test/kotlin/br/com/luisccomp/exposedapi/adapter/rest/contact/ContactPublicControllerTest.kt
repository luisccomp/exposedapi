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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactPublicControllerTest {

    lateinit var uuid: UUID

    @Autowired
    lateinit var mvc: MockMvc

    val objectMapper = ObjectMapper()

    companion object {
        const val CONTACT_API = "/public/api/v1/customers/%s/contacts"
    }

    fun getInvalidUUID(): UUID {
        var done = false
        var uuid = UUID.randomUUID()

        while (!done) {
            if (this.uuid != uuid) {
                done = true
            } else {
                uuid = UUID.randomUUID()
            }
        }

        return uuid
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

    @Test
    @DisplayName("Should return an error 400 when try to register a contact for a customer that does not exists")
    fun `register contact for a customer that doesn't exists test`() {
        val uuid = getInvalidUUID()

        val contactCreateRequest = createContactCreateRequest()

        val json = objectMapper.writeValueAsString(contactCreateRequest)

        val request = post(String.format(CONTACT_API, uuid.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Customer not found"))
    }

    @Test
    @DisplayName("Should return an error when try to register a contact with invalid fields")
    fun `register contact with invalid fields test`() {
        val json = "{\"name\": null, \"email\": null, \"phone\": null}"

        val request = post(String.format(CONTACT_API, uuid.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("Should update a customer's contact information")
    fun `update contact information test`() {
        val id = transaction {
            Contact.new {
                name = "Name"
                email = "contact@email.com"
                phone = "99917=9078"
                customer = Customer.findById(uuid)!!
            }.id.value
        }

        val contactCreateRequest = createContactCreateRequest()

        val json = objectMapper.writeValueAsString(contactCreateRequest)

        val url = "${String.format(CONTACT_API, uuid.toString())}/${id}"

        val request = put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("name").value(contactCreateRequest.name))
                .andExpect(jsonPath("email").value(contactCreateRequest.email))
                .andExpect(jsonPath("phone").value(contactCreateRequest.phone))
    }

    @Test
    @DisplayName("should return a status 400 when try to update a contact for a customer that does not exists")
    fun `update contact information for a customer that doesn't exists test`() {
        val uuid = getInvalidUUID()

        val json = objectMapper.writeValueAsString(createContactCreateRequest())

        val request = put("${String.format(CONTACT_API, uuid.toString())}/${1L}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Customer not found"))
    }

    @Test
    @DisplayName("Should return error 404 when try to update a contact that doesn't exists")
    fun `update contact that doesn't exists test`() {
        val contactCreateRequest = createContactCreateRequest()

        val json = objectMapper.writeValueAsString(contactCreateRequest)

        val request = put("${String.format(CONTACT_API, uuid.toString())}/${1L}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Contact not found"))
    }

    @Test
    @DisplayName("Should delete a contact")
    fun `delete contact test`() {
        val contactCreateRequest = createContactCreateRequest()

        val id = transaction {
            Contact.new {
                name = contactCreateRequest.name
                email = contactCreateRequest.email
                phone = contactCreateRequest.phone
                customer = Customer.findById(uuid)!!
            }.id.value
        }

        val request = delete("${String.format(CONTACT_API, uuid.toString())}/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isNoContent)

        val contact = transaction {
            Contact.findById(id)
        }

        assertThat(contact).isNull()
    }

    @Test
    @DisplayName("Should return error 404 when try to delete a contact that doesn't exists")
    fun `delete contact that doesn't exists test`() {
        val request = delete("${String.format(CONTACT_API, uuid.toString())}/${1L}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Contact not found"))
    }

    @Test
    @DisplayName("Should return error 400 when try to delete a contact of a customer that doesn't exists")
    fun `delete contact for a customer that doesn't exists test`() {
        val uuid = getInvalidUUID()

        val request = delete("${String.format(CONTACT_API, uuid.toString())}/${1L}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Customer not found"))
    }

    @Test
    @DisplayName("Should return contact information")
    fun `get contact details test`() {
        val contact = createContactCreateRequest()

        val id = transaction {
            Contact.new {
                name = contact.name
                email = contact.email
                phone = contact.phone
                customer = Customer.findById(uuid)!!
            }.id.value
        }

        val request =  get("${String.format(CONTACT_API, uuid)}/${id}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("name").value(contact.name))
                .andExpect(jsonPath("email").value(contact.email))
                .andExpect(jsonPath("phone").value(contact.phone))
    }

    @Test
    @DisplayName("Should return error 404 when try to get contact information that doesn't exists")
    fun `get contact that doesn't exists details test`() {
        val request = get("${String.format(CONTACT_API, uuid.toString())}/${1L}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Contact not found"))
    }

    @Test
    @DisplayName("Should return error 400 when try to get contact information of a customer that doesn't exists")
    fun `get contact details of a customer that doesn't exists details test`() {
        val uuid = getInvalidUUID()

        val request = get("${String.format(CONTACT_API, uuid.toString())}/${1}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Customer not found"))
    }

}