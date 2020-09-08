package br.com.luisccomp.exposedapi.adapter.service.customer

import br.com.luisccomp.exposedapi.adapter.rest.customer.CustomerPublicControllerTest.Companion.createCustomerCreateRequest
import br.com.luisccomp.exposedapi.domain.core.model.entity.customer.Customer
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJdbcTest
@ActiveProfiles("test")
class CustomerServiceTest {

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

}