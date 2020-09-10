package br.com.luisccomp.exposedapi.adapter.service.customer

import br.com.luisccomp.exposedapi.adapter.rest.customer.CustomerPublicControllerTest.Companion.createCustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import br.com.luisccomp.exposedapi.domain.port.service.customer.CustomerService
import br.com.luisccomp.exposedapi.shared.exception.BadRequestException
import br.com.luisccomp.exposedapi.shared.exception.NotFoundException
import org.assertj.core.api.Assertions.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@DataJdbcTest
@ActiveProfiles("test")
class CustomerServiceTest {

    private lateinit var customerService: CustomerService

    @BeforeEach
    fun setUp() {
        Database.connect(DataSourceBuilder.create()
                .url("jdbc:h2:mem:test")
                .driverClassName("org.h2.Driver")
                .username("sa")
                .password("")
                .build())

        transaction {
            SchemaUtils.createSchema(Schema(name = "customer"))
            SchemaUtils.create(CustomerTable)
        }

        transaction {
            CustomerTable.deleteAll()
        }

        customerService = CustomerServiceImpl()
    }

    @Test
    @DisplayName("Should delete a customer from database")
    fun `delete a customer from database test`() {
        val id = transaction {
            Customer.new {
                firstName = "FName"
                lastName = "LName"
                email = "customer_mail@cmail.com"
                phone = "99123-9764"
            }
        }.id.value

        customerService.delete(id)

        val count = transaction {
            Customer.find { CustomerTable.id eq id }
                    .count()
        }

        assertThat(count).isEqualTo(0)
    }

    @Test
    @DisplayName("Should delete a customer that doesn't exists on database")
    fun `delete a customer that doesn't exists test`() {
        val uuid = UUID(0L, 0L)

        val exception = catchThrowable { customerService.delete(uuid) }

        assertThat(exception).isInstanceOf(NotFoundException::class.java)
                .hasMessage("Customer not found")
    }

    @Test
    @DisplayName("Should return true if there is a customer with given ID")
    fun `checks if a customer exists given an ID test`() {
        val uuid = transaction {
            Customer.new {
                firstName = "FName"
                lastName = "LName"
                email = "e_mail@mail.com"
                phone = "99999-0000"
            }
        }.id.value

        val result = customerService.existsById(uuid)

        assertThat(result).isTrue()
    }

    @Test
    @DisplayName("Should return false when there is no customer with a given ID")
    fun `checks if there is no customer with given id test`() {
        val result = customerService.existsById(UUID(0L, 0L))

        assertThat(result).isFalse()
    }

    @Test
    @DisplayName("Should persist a customer on database")
    fun `persist customer on repository test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        val customer = transaction {
            Customer.new {
                firstName = customerCreateRequest.firstName
                lastName = customerCreateRequest.lastName
                email = customerCreateRequest.email
                phone = customerCreateRequest.phone
            }
        }

        assertThat(customer.id._value).isNotNull
        assertThat(customer.firstName).isEqualTo(customerCreateRequest.firstName)
        assertThat(customer.lastName).isEqualTo(customerCreateRequest.lastName)
        assertThat(customer.email).isEqualTo(customerCreateRequest.email)
        assertThat(customer.phone).isEqualTo(customerCreateRequest.phone)
    }

    @Test
    @DisplayName("Should throw an exception when try to persist a customer with duplicate email")
    fun `persist customer with duplicate email test`() {
        val customerCreateRequest = createCustomerCreateRequest()

        transaction {
            Customer.new {
                firstName = "FName"
                lastName  = "LName"
                email     = customerCreateRequest.email
                phone     = "99999-0000"
            }
        }

        val exception = catchThrowable { customerService.register(customerCreateRequest) }

        assertThat(exception).isInstanceOf(BadRequestException::class.java)
                .hasMessage("Email already in use")
    }

    @Test
    @DisplayName("Should return a customer's details")
    fun `find customer by id test`() {
        val info = createCustomerCreateRequest()

        val uuid = transaction {
            Customer.new {
                firstName = info.firstName
                lastName = info.lastName
                email = info.email
                phone = info.phone
            }
        }.id.value

        val customer = customerService.findById(uuid)

        assertThat(customer.id.value).isEqualTo(uuid)
        assertThat(customer.firstName).isEqualTo(info.firstName)
        assertThat(customer.lastName).isEqualTo(info.lastName)
        assertThat(customer.email).isEqualTo(info.email)
    }

    @Test
    @DisplayName("Should throw an error when give a id that doesn't exists")
    fun `fund customer that doesn't exists by id test`() {
        val uuid = UUID.randomUUID()

        val exception = catchThrowable { customerService.findById(uuid) }

        assertThat(exception).isInstanceOf(NotFoundException::class.java)
                .hasMessage("Customer not found")
    }

}