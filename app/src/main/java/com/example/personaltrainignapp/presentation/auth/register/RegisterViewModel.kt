package com.example.personaltrainignapp.presentation.auth.register

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.User
import com.example.personaltrainignapp.data.repository.AuthRepository
import com.example.personaltrainignapp.data.repository.UsersRepository
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _imageUploaded = MutableStateFlow<ScreenState<Uri?>>(ScreenState.Loading())
    val imageUploaded: MutableStateFlow<ScreenState<Uri?>> = _imageUploaded

    private val _registerState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val registerState = _registerState

    fun uploadImageAndGetUri(userId: String, imageUri: Uri) = viewModelScope.launch {
        try {
            usersRepository.uploadImageAndGetUri(userId, imageUri).let {
                if (it != null) _imageUploaded.value = ScreenState.Success(it)
                else _imageUploaded.value = ScreenState.Error("Error Loading image")
            }
        } catch (e: Exception) {
            _imageUploaded.value = ScreenState.Error(e.message.toString())
        }
    }

    fun register(user: User, password: String) = viewModelScope.launch {
        authRepository.signup(user, password).let {
            if (it == "Done") _registerState.value = ScreenState.Success(it)
            else _registerState.value = ScreenState.Error(it)
        }
    }

}