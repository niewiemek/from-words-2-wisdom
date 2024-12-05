package com.iteratec.words2wisdom

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info = Info(
            title = "From Words 2 Wisdom API",
            version = "0.0"
    )
)
object Api

fun main(args: Array<String>) {
	run(*args)
}

