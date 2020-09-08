package br.com.luisccomp.exposedapi.domain.port

class ResourceSchema {

    companion object {
        private const val PUBLIC_RESOURCE = "public"
        private const val BASE_RESOURCE = "api"
        const val V1_RESOURCE = "$BASE_RESOURCE/v1"
        const val PUBLIC_V1_RESOURCE = "$PUBLIC_RESOURCE/$BASE_RESOURCE/v1"
    }

    object CustomerResources {
        const val CUSTOMER_PUBLIC_RESOURCE = "$PUBLIC_V1_RESOURCE/customers"
        const val CUSTOMER_PUBLIC_RESOURCE_REGISTER = "/register"
    }

    object ContactResources {
        const val CONTACT_PUBLIC_RESOURCE = "${CustomerResources.CUSTOMER_PUBLIC_RESOURCE}/{uuid}/contacts"
        const val CONTACT_PUBLIC_RESOURCE_REGISTER = "/register"
    }

}