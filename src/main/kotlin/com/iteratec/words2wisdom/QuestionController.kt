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

@Controller("/question")
class QuestionController @Inject constructor(private val questionService: QuestionService) {

    @Operation(summary = "Sent a query to the LLM model.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "LLM response"),
    )
    @Post(uri = "/ask")
    fun ask(@Body request: AskRequest) = questionService.ask(request.question)


}

@Introspected
@Serdeable
data class AskRequest(val question: String)