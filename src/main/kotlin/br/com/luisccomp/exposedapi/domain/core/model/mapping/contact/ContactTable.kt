package br.com.luisccomp.exposedapi.domain.core.model.mapping.contact

import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.COLUMN_ID
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.SCHEMA_CONTACT
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_CUSTOMER_ID_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_EMAIL_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_EMAIL_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_NAME_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_NAME_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_PHONE_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.COLUMN_PHONE_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.FK_CONTACT_CUSTOMER_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.FK_CONTACT_CUSTOMER_ON_DELETE
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.FK_CONTACT_CUSTOMER_ON_UPDATE
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.ContactTableSchema.TABLE_NAME
import br.com.luisccomp.exposedapi.domain.core.model.mapping.customer.CustomerTable
import org.jetbrains.exposed.dao.id.LongIdTable

object ContactTable : LongIdTable(name = "$SCHEMA_CONTACT.$TABLE_NAME", columnName = COLUMN_ID) {

    val name = varchar(COLUMN_NAME_NAME, COLUMN_NAME_LENGTH)

    val email = varchar(COLUMN_EMAIL_NAME, COLUMN_EMAIL_LENGTH)

    val phone = varchar(COLUMN_PHONE_NAME, COLUMN_PHONE_LENGTH)

    val customerId = reference(COLUMN_CUSTOMER_ID_NAME,
                                CustomerTable,
                                fkName = FK_CONTACT_CUSTOMER_NAME,
                                onDelete = FK_CONTACT_CUSTOMER_ON_DELETE,
                                onUpdate = FK_CONTACT_CUSTOMER_ON_UPDATE)

}