package com.hann.storyapp.presentation.login

import androidx.lifecycle.*
import com.hann.storyapp.data.Resource
import com.hann.storyapp.domain.model.User
import com.hann.storyapp.domain.usecase.StoryUseCase
import com.hann.storyapp.ui.preference.UserPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(private val storyUseCase: StoryUseCase, private val pref: UserPreference) : ViewModel() {

    private val _isLoadingSplash = MutableStateFlow(true)
    val isLoadingSplash = _isLoadingSplash.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoadingSplash.value = false
        }
    }

    private val _state = MutableLiveData<LoginState>()
    val state : LiveData<LoginState> = _state

    fun loginUser(email: String, password: String){
        storyUseCase.loginUser(email, password).onEach {
                result ->
            when(result){
                is Resource.Loading -> {
                    _state.value = LoginState(isLoading = true)
                }
                is Resource.Error -> {
                    _state.value = LoginState(error = result.message ?: "Failed create an account")
                }
                is Resource.Success -> {
                    _state.value = LoginState(success = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getUser(): LiveData<User> {
        return pref.getUser().asLiveData()
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }



}