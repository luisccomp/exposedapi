package br.com.luisccomp.exposedapi.domain.core.model.mapping.customer

import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.COLUMN_ID
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.Companion.SCHEMA_CUSTOMER
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_EMAIL_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_EMAIL_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_FIRST_NAME_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_FIRST_NAME_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_LAST_NAME_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_LAST_NAME_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_PHONE_LENGTH
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.COLUMN_PHONE_NAME
import br.com.luisccomp.exposedapi.domain.core.constant.Schema.CustomerTableSchema.TABLE_NAME
import org.jetbrains.exposed.dao.id.UUIDTable

object CustomerTable : UUIDTable(name = "$SCHEMA_CUSTOMER.$TABLE_NAME", columnName = COLUMN_ID) {

    val firstName = varchar(COLUMN_FIRST_NAME_NAME, COLUMN_FIRST_NAME_LENGTH)

    val lastName = varchar(COLUMN_LAST_NAME_NAME, COLUMN_LAST_NAME_LENGTH)

    val email = varchar(COLUMN_EMAIL_NAME, COLUMN_EMAIL_LENGTH)
            .uniqueIndex("customer_mail_un")

    val phone = varchar(COLUMN_PHONE_NAME, COLUMN_PHONE_LENGTH)

}