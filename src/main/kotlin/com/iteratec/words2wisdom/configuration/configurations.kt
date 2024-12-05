package com.iteratec.words2wisdom.configuration

import io.micronaut.context.annotation.ConfigurationProperties
import kotlin.properties.Delegates

@ConfigurationProperties("embeddings-store")
class EmbeddingsStoreConfiguration {
    lateinit var host: String
    lateinit var collection: String
    var port by Delegates.notNull<Int>()

}

@ConfigurationProperties("embeddings-store.query")
class QueryConfiguration {
    var maxResults by Delegates.notNull<Int>()
    var minScore by Delegates.notNull<Double>()
}