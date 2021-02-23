package com.genies.avatar_api_sampleapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.UserSessionRepository
import com.genies.avatar_api_sampleapp.model.Resource
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.util.Event
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val userSessionRepository = UserSessionRepository
    val loginState: MutableLiveData<Event<Resource<Boolean>>> by lazy { MutableLiveData<Event<Resource<Boolean>>>() }
    val forgotPasswordState: MutableLiveData<Event<Resource<Boolean>>> by lazy { MutableLiveData<Event<Resource<Boolean>>>() }

    fun checkLogin() = viewModelScope.launch {
        userSessionRepository.getGeniesAuthSession()
            .onSuccess {
                loginState.value = Event(Success(true))
            }
    }

    fun logIn(username: String, password: String) = viewModelScope.launch {
        userSessionRepository.signIn(username.trim(), password)
            .onSuccess {
                loginState.value = Event(Success(true))
            }
            .onFailure {
                loginState.value = Event(Error(it.message))
            }

    }

}