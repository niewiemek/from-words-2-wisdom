package com.iteratec.words2wisdom

import com.iteratec.words2wisdom.configuration.Gpt4oChatModelConfig
import com.iteratec.words2wisdom.configuration.LlamaChatModelConfig
import dev.langchain4j.memory.chat.MessageWindowChatMemory
import dev.langchain4j.model.ollama.OllamaChatModel
import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.rag.DefaultRetrievalAugmentor
import dev.langchain4j.rag.RetrievalAugmentor
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever
import dev.langchain4j.rag.query.transformer.CompressingQueryTransformer
import dev.langchain4j.rag.query.transformer.QueryTransformer
import dev.langchain4j.service.AiServices
import dev.langchain4j.service.SystemMessage
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class ChatService @Inject constructor(
    private val llmamaChatModelConf: LlamaChatModelConfig,
    private val gpt4oChatModelConf: Gpt4oChatModelConfig,
    private val embeddingsService: EmbeddingsService,
    private val embeddingsStore: EmbeddingsStore
) {
    private val openAiChatModel = OpenAiChatModel.builder()
        .apiKey(gpt4oChatModelConf.apiKey)
        .baseUrl(gpt4oChatModelConf.baseUrl)
        .modelName(gpt4oChatModelConf.name)
        .apiKey(gpt4oChatModelConf.apiKey)
        .temperature(0.9) // magic parameter
        .build()

    private val ollamaChatModel = OllamaChatModel.builder()
        .baseUrl(llmamaChatModelConf.baseUrl)
        .modelName(llmamaChatModelConf.name)
        .temperature(0.3)
        .build()

    // just to keep it in one place
    private val chatModel = openAiChatModel

    private val queryTransformer: QueryTransformer = CompressingQueryTransformer(chatModel)

    private val contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingsStore.store)
        .embeddingModel(embeddingsService.embeddingModel)
        .minScore(0.6)
        .maxResults(7)
        .build()

    private val retrievalAugmentor: RetrievalAugmentor = DefaultRetrievalAugmentor.builder()
        .queryTransformer(queryTransformer)
        .contentRetriever(contentRetriever)
        .build()

    private val chatService = AiServices.builder(Assistant::class.java)
        .chatLanguageModel(chatModel)
        .retrievalAugmentor(retrievalAugmentor)
//        .contentRetriever(contentRetriever)
        .chatMemory(MessageWindowChatMemory.builder().maxMessages(10).build())
        .build()

    fun chat(question: String): String = chatService.chat(question)
}

interface Assistant {

    @SystemMessage("You are a servant in an Imperial library. Refer to my as my Lord. Use medieval styled english. When answering concentrate in a very clear answer to the question basing on the provided input.")
    fun chat(question: String): String
}