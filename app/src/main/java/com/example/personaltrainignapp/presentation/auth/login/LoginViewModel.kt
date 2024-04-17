package com.example.personaltrainignapp.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.repository.AuthRepository
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loggedIn = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val loggedIn = _loggedIn

    fun login(email: String, password: String) = viewModelScope.launch {
        try {
            authRepository.login(email, password).let {
                _loggedIn.value = ScreenState.Success(it)
            }
        } catch (e: Exception) {
            _loggedIn.value = ScreenState.Error(e.message.toString())
            Log.d("ResultExcVM", e.message.toString())
        }
    }

}