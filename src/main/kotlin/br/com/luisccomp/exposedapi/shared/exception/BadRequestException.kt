package br.com.luisccomp.exposedapi.shared.exception

import java.lang.RuntimeException

class BadRequestException(message: String) : RuntimeException(message)
