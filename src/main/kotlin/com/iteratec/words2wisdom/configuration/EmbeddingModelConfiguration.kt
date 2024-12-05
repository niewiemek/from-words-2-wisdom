package com.iteratec.words2wisdom.configuration

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("model.embedding")
class EmbeddingModelConfiguration {
    lateinit var baseUrl: String
    lateinit var name: String
}

