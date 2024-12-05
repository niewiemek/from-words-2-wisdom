package com.iteratec.words2wisdom

import com.iteratec.words2wisdom.configuration.EmbeddingModelConfiguration
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.model.embedding.EmbeddingModel
import dev.langchain4j.model.ollama.OllamaEmbeddingModel
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class EmbeddingsService @Inject constructor(
    private val embeddingModelConfiguration: EmbeddingModelConfiguration,
    private val embeddingsStore: EmbeddingsStore
) {

    val embeddingModel: EmbeddingModel = OllamaEmbeddingModel.builder()
        .modelName(embeddingModelConfiguration.name)
        .baseUrl(embeddingModelConfiguration.baseUrl)
        .build()

    fun embed(text: String): Embedding {
        val embedding = embeddingModel.embed(text).content()
        embeddingsStore.add(embedding.vector(), text)

        return embedding
    }
}