package com.iteratec.words2wisdom.documents

import com.google.gson.Gson
import com.iteratec.words2wisdom.EmbeddingsController
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging


object DocumentsReader

private val logger: KLogger = KotlinLogging.logger {}

private val client = HttpClient {
    install(Logging) {
        level = LogLevel.NONE
    }
}

fun main(args: Array<String>) {
    val fluff = readPdf("/documents/fluff/40k_fluff-bible.pdf")
    val rules = readPdf("/documents/rules/ENG_40K9_Basic_Rules.pdf")

    val splitter = DocumentSplitters.recursive(
        500,
        50,
        HuggingFaceTokenizer()
    )

    val segments = splitter
        .split(fluff).apply {
            addAll(splitter.split(rules))
        }

    logger.info { "Created ${segments.size}." }

    segments.forEach { textSegment ->
        runBlocking {
            client.post("http://localhost:8080/embeddings/embed") {
                contentType(ContentType.Application.Json)
                setBody(
                    EmbeddingsController.EmbedRequest(
                        textSegment.text()
                    ).let { Gson().toJson(it) }
                )
            }
        }
    }
    logger.info { "${segments.size} segments persisted." }
}

fun readPdf(pdfPath: String): Document = loadDocument(
    DocumentsReader::class.java.getResource(pdfPath)?.path,
    ApachePdfBoxDocumentParser()
)
