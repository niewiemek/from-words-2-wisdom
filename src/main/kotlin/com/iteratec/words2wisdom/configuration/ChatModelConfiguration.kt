package com.iteratec.words2wisdom.configuration

import io.micronaut.context.annotation.ConfigurationProperties

open class ChatModelConfiguration {
    lateinit var name: String
    lateinit var baseUrl: String
    lateinit var apiKey: String
}

@ConfigurationProperties("model.chat.llama")
class LlamaChatModelConfig: ChatModelConfiguration()

@ConfigurationProperties("model.chat.gpt4o")
class Gpt4oChatModelConfig: ChatModelConfiguration()