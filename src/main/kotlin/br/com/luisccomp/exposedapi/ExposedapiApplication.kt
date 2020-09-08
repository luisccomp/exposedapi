package br.com.luisccomp.exposedapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExposedapiApplication

fun main(args: Array<String>) {
	runApplication<ExposedapiApplication>(*args)
}
