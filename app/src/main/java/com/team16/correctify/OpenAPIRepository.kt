package com.team16.correctify

import android.util.Log
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.edits.EditsRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OpenAPIRepository(
    private val openAI: OpenAI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    @OptIn(BetaOpenAI::class)
    suspend fun fixTextMistakes(
        prompt: String
    ): String {
        val instructions: String = "You are a AI grammar and punctuation system. " +
                "You will fix the mistakes in the text you are given. " +
                "You will not include anything but the fixed text in your responses."

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.System,
                    content = instructions,
                ),
                ChatMessage(
                    role = ChatRole.User,
                    content = prompt
                )
            )
        )

        val completion: ChatCompletion = openAI.chatCompletion(chatCompletionRequest)

        return completion.choices[0].message!!.content
    }

    // Uses a different API route
    // Didn't seem to work as well.
    suspend fun fixTextMistakes2(
        prompt: String
    ): String {
        val edit = openAI.edit(
            request = EditsRequest(
                model = ModelId("text-davinci-edit-001"),
                input = prompt,
                instruction = "Fix the spelling and grammar mistakes"
            )
        )

        Log.d("EditResult", edit.toString())

        return edit.choices[0].text
    }

}