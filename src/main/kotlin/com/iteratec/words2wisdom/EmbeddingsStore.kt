package com.iteratec.words2wisdom

import com.iteratec.words2wisdom.configuration.EmbeddingsStoreConfiguration
import dev.langchain4j.data.document.Metadata
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EmbeddingsStore @Inject constructor(
    private val configuration: EmbeddingsStoreConfiguration,
) {

    val store = QdrantEmbeddingStore.builder()
        .host(configuration.host)
        .port(configuration.port)
        .collectionName(configuration.collection)
        .build()

    fun add(vector: FloatArray, payload: String): String =
        store.add(Embedding(vector), TextSegment(payload, Metadata()))
}