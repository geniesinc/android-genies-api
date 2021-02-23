package com.genies.avatar_api_sampleapp.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.UserSessionRepository
import com.genies.avatar_api_sampleapp.model.Resource
import com.genies.avatar_api_sampleapp.model.Success
import com.genies.avatar_api_sampleapp.model.Error
import com.genies.avatar_api_sampleapp.util.Event
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class SignUpConfirmationViewModel : ViewModel() {

    val signupConfirmationStatus: MutableLiveData<Event<Resource<Boolean>>> by lazy { MutableLiveData<Event<Resource<Boolean>>>() }
    private val userSessionRepository = UserSessionRepository

    fun confirmSignUp(username: String, confirmationCode: String) = viewModelScope.launch {
        userSessionRepository.confirmSignUp(username, confirmationCode)
            .onSuccess {
                signupConfirmationStatus.value = Event(Success(true))
            }
            .onFailure {
                signupConfirmationStatus.value = Event(Error(it.message))
            }


    }
}