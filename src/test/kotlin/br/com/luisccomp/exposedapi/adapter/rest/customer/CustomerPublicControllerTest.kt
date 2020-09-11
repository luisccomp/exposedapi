package br.com.luisccomp.exposedapi.adapter.rest.customer

import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.contact.ContactTable
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.core.model.request.customer.CustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.port.ResourceSchema.CustomerResources.CUSTOMER_PUBLIC_RESOURCE
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasSize
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerPublicControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    private val objectMapper = ObjectMapper()

    companion object {
        fun createCustomerCreateRequest(): CustomerCreateRequest =
                CustomerCreateRequest("CustomerName",
                        "CustomerLastName",
                        "customer_email@provider.com",
                        "99999-1234")
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
            SchemaUtils.createSchema(Schema(br.com.luisccomp.exposedapi.domain.core.constant.Schema.SCHEMA_CUSTOMER))
            SchemaUtils.createSchema(Schema(br.com.luisccomp.exposedapi.domain.core.constant.Schema.SCHEMA_CONTACT))
            SchemaUtils.create(CustomerTable)
            SchemaUtils.create(ContactTable)
        }
    }

    @BeforeEach
    fun clearDb() {
        transaction {
            CustomerTable.deleteAll()
        }
    }

    @Test
    @DisplayName("Should return customer's details")
    fun `return customer's details test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        val uuid = transaction {
            Customer.new {
                firstName = customerCreateRequest.firstName
                lastName = customerCreateRequest.lastName
                email = customerCreateRequest.email
                phone = customerCreateRequest.phone
            }
        }.id.value

        val request = get("/$CUSTOMER_PUBLIC_RESOURCE/${uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isOk)
                .andExpect(jsonPath("firstName").value(customerCreateRequest.firstName))
                .andExpect(jsonPath("lastName").value(customerCreateRequest.lastName))
                .andExpect(jsonPath("email").value(customerCreateRequest.email))
                .andExpect(jsonPath("phone").value(customerCreateRequest.phone))
    }

    @Test
    @DisplayName("Should return error 404 when try to obtain details of a customer that doesn't exists")
    fun `return customer that doesn't exists details test`() {
        val uuid = UUID.randomUUID()

        val request = get("/$CUSTOMER_PUBLIC_RESOURCE/${uuid}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        mvc.perform(request)
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("status").value(404))
                .andExpect(jsonPath("message").value("Customer not found"))
    }

    @Test
    @DisplayName("Should store a customer on database")
    fun `register customer test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        val json = objectMapper.writeValueAsString(customerCreateRequest)

        val request = post("/$CUSTOMER_PUBLIC_RESOURCE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isCreated)

        val customer = transaction {
            Customer.find { CustomerTable.email eq customerCreateRequest.email }.firstOrNull()
        }

        assertThat(customer).isNotNull
        assertThat(customer?.firstName).isEqualTo(customerCreateRequest.firstName)
        assertThat(customer?.lastName).isEqualTo(customerCreateRequest.lastName)
        assertThat(customer?.email).isEqualTo(customerCreateRequest.email)
        assertThat(customer?.phone).isEqualTo(customerCreateRequest.phone)
    }

    @Test
    @DisplayName("Should return an error 400 when try to register a customer with duplicate email")
    fun `register customer with duplicate email test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        transaction {
            Customer.new {
                firstName = "FirstName"
                lastName = "LastName"
                email = customerCreateRequest.email
                phone = "99999-0000"
            }
        }

        val json = objectMapper.writeValueAsString(customerCreateRequest)

        val request = post("/$CUSTOMER_PUBLIC_RESOURCE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("Email already in use"))
    }

    @Test
    @DisplayName("Should throw error 400 when try to register client with invalid fields")
    fun `register customer with invalid fields test`() {
        val json = "{\"firstName\": null, \"lastName\": null, \"email\": null, \"phone\": null}"

        val request = post("/$CUSTOMER_PUBLIC_RESOURCE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)

        mvc.perform(request)
                .andExpect(status().isBadRequest)
        //        .andExpect(jsonPath("errors", hasSize(4)))
    }

}