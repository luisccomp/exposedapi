package br.com.luisccomp.exposedapi.adapter.service.contact

import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.SCHEMA_CONTACT
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.SCHEMA_CUSTOMER
import br.com.luisccomp.exposedapi.domain.core.model.entity.contact.Contact
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.core.model.request.contact.ContactCreateRequest
import br.com.luisccomp.exposedapi.domain.port.service.contact.ContactService
import br.com.luisccomp.exposedapi.shared.exception.BadRequestException
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@DataJdbcTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ContactServiceTest {

    private lateinit var uuid: UUID

    private lateinit var contactService: ContactService

    companion object {
        fun createContactCreateRequest(): ContactCreateRequest =
                ContactCreateRequest("Name", "contact_name@email.com", "99998-7315")

        fun createContactCreateRequest(name: String, email: String, phone: String): ContactCreateRequest =
                ContactCreateRequest(name, email, phone)
    }

    fun getInvalidUUID(): UUID {
        var uuid = UUID.randomUUID()
        var done = false

        // Generating an invalid UUID and assure that this one will not be equals to the valid one previously generated
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
            SchemaUtils.createSchema(Schema(SCHEMA_CUSTOMER))
            SchemaUtils.createSchema(Schema(SCHEMA_CONTACT))
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
    fun clear() {
        transaction {
            ContactTable.deleteAll()
        }

        contactService = ContactServiceImpl()
    }

    @Test
    @DisplayName("Should add a contact for a customer")
    fun `register a contact for a customer test`() {
        val contactCreateRequest = createContactCreateRequest()

        val id = contactService.register(uuid, contactCreateRequest)

        val contact = transaction {
            Contact.findById(id)
        }

        assertThat(contact).isNotNull
        assertThat(contact?.id?.value).isEqualTo(id)
        assertThat(contact?.name).isEqualTo(contactCreateRequest.name)
        assertThat(contact?.email).isEqualTo(contactCreateRequest.email)
        assertThat(contact?.phone).isEqualTo(contactCreateRequest.phone)
    }

    @Test
    @DisplayName("Should throw an exception when try to add a contact for a customer that doesn't exists")
    fun `register a contact for a customer that doesnt exists test`() {
        val uuid = getInvalidUUID()

        val exception = catchThrowable { contactService.register(uuid, createContactCreateRequest()) }

        assertThat(exception).isInstanceOf(BadRequestException::class.java)
                .hasMessage("Customer not found")
    }

    @Test
    @DisplayName("Should delete a contact of a customer")
    fun `delete a contact of a customer test`() {
        val contactCreateRequest = createContactCreateRequest()

        val id = transaction {
            val customerFound = Customer.findById(uuid) as Customer

            Contact.new {
                name = contactCreateRequest.name
                email = contactCreateRequest.email
                phone = contactCreateRequest.phone
                customer = customerFound
            }.id.value
        }

        contactService.delete(uuid, id)

        val count = transaction {
            Contact.find { (ContactTable.customerId eq uuid) and  (ContactTable.id eq id) }
                    .count()
        }

        assertThat(count).isEqualTo(0L)
    }

    @Test
    @DisplayName("Should throw an error when try to delete a contact that doesn't exists")
    fun `delete a contact that doesn't exists test`() {
        val exception = catchThrowable { contactService.delete(uuid, 1L)  }

        assertThat(exception).isInstanceOf(NotFoundException::class.java)
                .hasMessage("Contact not found")
    }

    @Test
    @DisplayName("Should throw an error when try to delete a contact of a customer that doesn't exists")
    fun `delete a contact of a customer that doesn't exists test`() {
        val uuid = getInvalidUUID()

        val exception = catchThrowable { contactService.delete(uuid, 1L) }

        assertThat(exception).isInstanceOf(BadRequestException::class.java)
                .hasMessage("Customer not found")
    }

    @Test
    @DisplayName("Should update a contact information")
    fun `update a contact of a customer test`() {
        val id = transaction {
            val customerFound = Customer.findById(uuid)!!

            Contact.new {
                name = "Contact Name"
                email = "contact_email@pmail.com"
                phone = "99999-8888"
                customer = customerFound
            }.id.value
        }

        val contactCreateRequest = createContactCreateRequest()

        val contact = contactService.update(uuid, contactCreateRequest, id)

        assertThat(contact.name).isEqualTo(contactCreateRequest.name)
        assertThat(contact.email).isEqualTo(contactCreateRequest.email)
        assertThat(contact.phone).isEqualTo(contact.phone)
    }

    @Test
    @DisplayName("Should throw an error when try to update a contact that doesn't exists")
    fun `update a contact that doesn't exists test`() {
        val contactCreateRequest = createContactCreateRequest()

        val exception = catchThrowable { contactService.update(uuid, contactCreateRequest, 1L) }

        assertThat(exception).isInstanceOf(NotFoundException::class.java)
                .hasMessage("Contact not found")
    }

    @Test
    @DisplayName("Should throw an error when try to update a contact of a customer that doesn't exists")
    fun `update a contact of a customer that doesn't exists`() {
        val uuid = getInvalidUUID()

        val contactCreateRequest = createContactCreateRequest()

        val exception = catchThrowable { contactService.update(uuid, contactCreateRequest, 1L) }

        assertThat(exception).isInstanceOf(BadRequestException::class.java)
                .hasMessage("Customer not found")
    }

    @Test
    @DisplayName("Should return contact's details")
    fun `get contact's details test`() {
        val contactCreateRequest = createContactCreateRequest()

        val id = transaction {
            val customerFound = Customer.findById(uuid)!!

            Contact.new {
                name = contactCreateRequest.name
                email = contactCreateRequest.email
                phone = contactCreateRequest.phone
                customer = customerFound
            }.id.value
        }

        val contact = contactService.findById(uuid, id)

        assertThat(contact?.id?.value).isEqualTo(id)
        assertThat(contact?.name).isEqualTo(contactCreateRequest.name)
        assertThat(contact?.email).isEqualTo(contactCreateRequest.email)
        assertThat(contact?.phone).isEqualTo(contactCreateRequest.phone)
    }

    @Test
    @DisplayName("Should throw an error when try to get contact's information that doesn't exists")
    fun `get contact's details that doesn't exists test`() {
        val exception = catchThrowable { contactService.findById(uuid, 1L) }

        assertThat(exception).isInstanceOf(NotFoundException::class.java)
                .hasMessage("Contact not found")
    }

    @Test
    @DisplayName("Should throw an error when try to get details of a contact of a customer that doesn't exists")
    fun `get contact's details of a customer that doesn't exists`() {
        val exception = catchThrowable { contactService.findById(getInvalidUUID(), 1L) }

        assertThat(exception).isInstanceOf(BadRequestException::class.java)
                .hasMessage("Customer not found")
    }

}