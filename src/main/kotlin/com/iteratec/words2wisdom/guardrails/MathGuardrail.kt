package com.iteratec.words2wisdom.guardrails

import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.guardrail.InputGuardrail
import dev.langchain4j.guardrail.InputGuardrailResult
import mu.KLogger
import mu.KotlinLogging

private val logger: KLogger = KotlinLogging.logger {}


class MathGuardrail : InputGuardrail {

    override fun validate(userMessage: UserMessage): InputGuardrailResult {
        logger.info { "User query is verified for forbidden content" }
        return if(userMessage.singleText().contains("calculate")) {
            fatal("Sorry my Lord math has been prohibited")
        } else {
            success()
        }
    }
}