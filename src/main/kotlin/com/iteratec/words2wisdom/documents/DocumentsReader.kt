package com.iteratec.words2wisdom.documents

import com.iteratec.words2wisdom.EmbeddingsService
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import io.micronaut.runtime.Micronaut
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging


object DocumentsReader

private val logger: KLogger = KotlinLogging.logger {}

fun main() {
    val fluff = readPdf("/documents/fluff/40k_fluff-bible.pdf")
    val rules = readPdf("/documents/rules/ENG_40K9_Basic_Rules.pdf")

    val splitter = DocumentSplitters.recursive(
        5000,
        500
    )

    val segments = splitter.splitAll(listOf(fluff, rules))

    logger.info { "Created ${segments.size}." }

    val context = Micronaut.run()
    val embeddingsService = context.getBean(EmbeddingsService::class.java)

    segments.forEach { textSegment ->
        runBlocking {
            embeddingsService.embed(textSegment.text(), textSegment.metadata())
        }
    }
    context.close()
    logger.info { "${segments.size} segments persisted." }
}

fun readPdf(pdfPath: String): Document = loadDocument(
    DocumentsReader::class.java.getResource(pdfPath)?.path,
    ApachePdfBoxDocumentParser()
)
