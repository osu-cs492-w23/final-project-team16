package com.team16.correctify

import android.util.Log
import com.aallam.openai.api.edits.EditsRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class OpenAPIRepository(
    private val openAI: OpenAI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun fixTextMistakes(
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