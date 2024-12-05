package com.iteratec.words2wisdom

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import mu.KotlinLogging
import kotlin.Exception


private val logger = KotlinLogging.logger {}

@Produces
@Singleton
@Requires(classes = [Exception::class, ExceptionHandler::class])
class ExceptionHandler :
  ExceptionHandler<Exception?, HttpResponse<*>> {

  override fun handle(request: HttpRequest<*>?, exception: Exception?): HttpResponse<JsonError> {
    logger.error(exception) { exception?.message }
    return HttpResponse.serverError(JsonError(exception?.message))
  }
}
