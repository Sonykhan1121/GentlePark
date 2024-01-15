package com.example.gentlepark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gentlepark.data.User
import com.example.gentlepark.util.RegisterFieldState
import com.example.gentlepark.util.RegisterValidation
import com.example.gentlepark.util.Resource
import com.example.gentlepark.util.validateEmail
import com.example.gentlepark.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth

):ViewModel() {
 private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()
    private val _resetPassword  = MutableSharedFlow<Resource<String>>()
    val  resetPassword = _resetPassword.asSharedFlow()
    private val _validation = Channel<RegisterFieldState> ()
    val validation = _validation.receiveAsFlow()

    fun login(email:String,password :String)
    {
        if(CheckValidation(email,password))
        {
            viewModelScope.launch {
                _login.emit(Resource.Loading())
            }
            firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        it.user?.let {
                            _login.emit(Resource.Success(it))
                        }
                    }
                }
                .addOnFailureListener{
                    viewModelScope.launch {
                        _login.emit(Resource.Error(it.message.toString()))

                    }
                }
        }
        else
        {
            val registerFieldState = RegisterFieldState(
                validateEmail(email),
                validatePassword(password)
            )
            runBlocking {

                _validation.send(registerFieldState)
            }
        }

    }
    fun resetPassword(email: String)
    {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    viewModelScope.launch {

                        _resetPassword.emit(Resource.Success(email))
                    }
                }
                .addOnFailureListener {
            viewModelScope.launch {
                _resetPassword.emit(Resource.Error(it.message.toString()))
            }
                }



    }
    private fun CheckValidation(email: String, password: String):Boolean {
        val emailValidation = validateEmail(email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }
}