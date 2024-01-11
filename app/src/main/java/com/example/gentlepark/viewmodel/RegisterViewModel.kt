package com.example.gentlepark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth
) :ViewModel() {

    private val _register= MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register
    private val  _validation   = Channel<RegisterFieldState> ()
     val  validation   = _validation.receiveAsFlow()
    fun createAccountwithEmailandPassword(user: User, password: String) {
        Log.d("test", "createAccountwithEmailandPassword method called") // Add this line

        if (CheckValidation(user, password))
        {
            runBlocking {
                _register.emit(Resource.Loading())
            }


            Log.d("test", "Creating account for user: $user") // Add this line
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { it ->
                    it.user?.let {
                        _register.value = Resource.Success(it)
                        Log.d("test", "Account creation success: $it") // Add this line
                    }

                }
                .addOnFailureListener {
                    _register.value = Resource.Error(it.message.toString())
                    Log.d("test", "Error in RegisterViewModel: ${it.message}")
                }
        }
        else{
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {

            _validation.send(registerFieldState)
            }
        }

    }

    private fun CheckValidation(user: User, password: String):Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }


}