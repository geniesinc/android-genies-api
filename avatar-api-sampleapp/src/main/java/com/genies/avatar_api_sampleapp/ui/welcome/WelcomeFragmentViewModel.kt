package com.genies.avatar_api_sampleapp.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genies.avatar_api_sampleapp.domain.UserSessionRepository
import com.genies.avatar_api_sampleapp.util.Event
import com.github.michaelbull.result.onSuccess
import kotlinx.coroutines.launch

class WelcomeFragmentViewModel : ViewModel() {
    private val userSessionRepository = UserSessionRepository

    private val _signInState: MutableLiveData<Event<Boolean>> by lazy { MutableLiveData<Event<Boolean>>() }
    val signInState: LiveData<Event<Boolean>> = _signInState
    fun checkSessionStatus() = viewModelScope.launch {
        // Try and get the Genies Auth Session
        userSessionRepository.getGeniesAuthSession().onSuccess {
            _signInState.value = Event(true)
        }

    }
}