package br.com.luisccomp.exposedapi.domain.core.constant

import org.jetbrains.exposed.sql.ReferenceOption

class Schema {

    companion object {
        const val SCHEMA_CUSTOMER = "customer"

        const val SCHEMA_CONTACT = "contact"

        const val COLUMN_ID = "id"
    }

    object CustomerTableSchema {
        const val TABLE_NAME = "customer"

        const val COLUMN_FIRST_NAME_NAME = "first_name"
        const val COLUMN_FIRST_NAME_LENGTH = 250

        const val COLUMN_LAST_NAME_NAME = "last_name"
        const val COLUMN_LAST_NAME_LENGTH = 250

        const val COLUMN_EMAIL_NAME = "email"
        const val COLUMN_EMAIL_LENGTH = 50

        const val COLUMN_PHONE_NAME = "phone"
        const val COLUMN_PHONE_LENGTH = 250
    }

    object ContactTableSchema {
        const val TABLE_NAME = "contact"

        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_LENGTH = 250

        const val COLUMN_EMAIL_NAME = "email"
        const val COLUMN_EMAIL_LENGTH = 250

        const val COLUMN_PHONE_NAME = "phone"
        const val COLUMN_PHONE_LENGTH = 250

        const val COLUMN_CUSTOMER_ID_NAME = "customer_id"

        const val FK_CONTACT_CUSTOMER_NAME = "fk_contact_customer"
        val FK_CONTACT_CUSTOMER_ON_DELETE = ReferenceOption.CASCADE
        val FK_CONTACT_CUSTOMER_ON_UPDATE = ReferenceOption.CASCADE
    }

}