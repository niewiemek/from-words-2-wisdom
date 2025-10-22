package com.iteratec.words2wisdom

import com.iteratec.words2wisdom.configuration.RemoteChatConfig
import com.iteratec.words2wisdom.configuration.LlamaChatModelConfig
import com.iteratec.words2wisdom.guardrails.MathGuardrail
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
    private val remoteChatModelConf: RemoteChatConfig,
    private val embeddingsService: EmbeddingsService,
    private val embeddingsStore: EmbeddingsStore
) {
    private val openAiChatModel = OpenAiChatModel.builder()
        .apiKey(remoteChatModelConf.apiKey)
        .baseUrl(remoteChatModelConf.baseUrl)
        .modelName(remoteChatModelConf.name)
        .logRequests(true)
        .temperature(0.0) // magic parameter
        .build()

    private val ollamaChatModel = OllamaChatModel.builder()
        .baseUrl(llmamaChatModelConf.baseUrl)
        .modelName(llmamaChatModelConf.name)
        .logRequests(true)
        .temperature(0.0)
        .build()

    // just to keep it in one place
    private val chatModel = ollamaChatModel
//    private val chatModel = openAiChatModel

    private val queryTransformer: QueryTransformer = CompressingQueryTransformer(chatModel)

    private val contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingsStore.store)
        .embeddingModel(embeddingsService.embeddingModel)
        .minScore(0.6)
        .maxResults(5)
        .build()

    private val retrievalAugmentor: RetrievalAugmentor = DefaultRetrievalAugmentor.builder()
        .queryTransformer(queryTransformer)
        .contentRetriever(contentRetriever)
        .build()

    private val chatService = AiServices.builder(Assistant::class.java)
        .chatModel(chatModel)
        .retrievalAugmentor(retrievalAugmentor)
        .inputGuardrails(listOf(MathGuardrail()))
        .chatMemory(MessageWindowChatMemory.builder().maxMessages(10).build())
        .build()

    fun chat(question: String): String = chatService.chat(question)
}

interface Assistant {

//    @SystemMessage("You are a servant in an Imperial library. Refer to my as my Lord. Use medieval styled english. Be precise on the answer limit it to 100 words.")
    fun chat(question: String): String
}