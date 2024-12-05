package com.iteratec.words2wisdom

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.inject.Inject

@Controller("/embeddings")
class EmbeddingsController @Inject constructor(
    private val embeddingsService: EmbeddingsService,
) {

    @Post(uri="/embed")
    @Operation(summary = "Embed the given string using defined language model")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "The text vector"),
    )
    fun embed(@Body request: EmbedRequest): FloatArray = embeddingsService.embed(request.value).vector()

    @Introspected
    @Serdeable
    data class EmbedRequest(val value: String)

}