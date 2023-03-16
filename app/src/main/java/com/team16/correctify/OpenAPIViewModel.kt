package com.team16.correctify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.launch


class OpenAPIViewModel: ViewModel() {
    private val repository = OpenAPIRepository(OpenAI(OPENAI_KEY))

    private val _lastResponse = MutableLiveData<String>(null)
    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _errorText = MutableLiveData<String>(null)

    val lastResponse: LiveData<String> = _lastResponse
    val isLoading: LiveData<Boolean> = _isLoading
    val errorText: LiveData<String> = _errorText

    fun fixTextMistakes(
        prompt: String
    ) {

        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.fixTextMistakes(prompt)
            _lastResponse.value = result
            _isLoading.value = false
        }
    }
}