package com.iteratec.words2wisdom

import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class QuestionService @Inject constructor(
    private val chatService: ChatService,
) {

    fun ask(question: String) =
        chatService.chat(question)
}