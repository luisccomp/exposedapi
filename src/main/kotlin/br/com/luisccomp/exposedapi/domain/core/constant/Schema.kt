package br.com.luisccomp.exposedapi.domain.core.constant

class Schema {

    companion object {
        const val SCHEMA_CUSTOMER = "customer"
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

}