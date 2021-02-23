package com.genies.avatar_api_sampleapp.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.UserSessionRepository
import com.genies.avatar_api_sampleapp.model.Resource
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.util.Event

import kotlinx.coroutines.launch
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess

class SignUpViewModel : ViewModel() {

    val signupStatus: MutableLiveData<Event<Resource<Boolean>>> by lazy { MutableLiveData<Event<Resource<Boolean>>>() }
    private val userSessionRepository = UserSessionRepository

    fun signUp(username: String, password: String) = viewModelScope.launch {
        userSessionRepository.signUp(username, password)
            .onSuccess {
                signupStatus.value = Event(Success(true))
            }
            .onFailure {
                signupStatus.value = Event(Error(it.message))
            }
    }
}