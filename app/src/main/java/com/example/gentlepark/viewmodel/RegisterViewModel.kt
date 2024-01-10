package com.example.gentlepark.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.gentlepark.data.User
import com.example.gentlepark.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth:FirebaseAuth
) :ViewModel() {

    private val _register= MutableStateFlow<Resource<FirebaseUser>>(Resource.Unspecified())
    val register: Flow<Resource<FirebaseUser>> = _register
    fun createAccountwithEmailandPassword(user: User, password: String) {
        Log.d("test", "createAccountwithEmailandPassword method called") // Add this line

        runBlocking {
            _register.emit(Resource.Loading())
        }

        try {
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
        } catch (e: Exception) {
            // Catch any exceptions that might occur during the account creation process
            _register.value = Resource.Error(e.message.toString())
            Log.e("test", "Exception during account creation: ${e.message}", e)
        }
    }



}