package com.team16.correctify.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.client.OpenAI
import com.team16.correctify.data.LoadingStatus
import com.team16.correctify.data.OpenAPIRepository
import kotlinx.coroutines.launch


class OpenAPIViewModel: ViewModel() {
    private val repository = OpenAPIRepository(OpenAI(OPENAI_KEY))

    private val _lastResponse = MutableLiveData<String>(null)
    private val _loadingStatus =
        MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    private val _errorText = MutableLiveData<String>(null)

    val lastResponse: LiveData<String> = _lastResponse
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus
    val errorText: LiveData<String> = _errorText

    fun fixTextMistakes(
        prompt: String
    ) {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.fixTextMistakes(prompt)
            _lastResponse.value = result
            _loadingStatus.value = LoadingStatus.SUCCESS
        }
    }
}