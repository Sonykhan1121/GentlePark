package com.example.gentlepark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gentlepark.data.Product
import com.example.gentlepark.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCatergoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore

) :ViewModel() {
    private val _specialProducts  = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts:StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestdealsProducts  = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestdealsProducts:StateFlow<Resource<List<Product>>> = _bestdealsProducts

    private val _bestProducts  = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts:StateFlow<Resource<List<Product>>> = _bestProducts
    init {
        fetchSpecialProducts()
        fetchBestDealsProducts()
        fetchbestProducts()
    }

    fun fetchSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("products").whereEqualTo("category","Special products").get()
            .addOnSuccessListener {result ->
                val specialProductslist = result.toObjects(Product::class.java)
                viewModelScope.launch {

                _specialProducts.emit(Resource.Success(specialProductslist))
                }

            }
            .addOnFailureListener {
                    viewModelScope.launch {
                        _specialProducts.emit(Resource.Error(it.message.toString()))
                    }
            }
    }
    fun fetchBestDealsProducts(){
        viewModelScope.launch {
            _bestdealsProducts.emit(Resource.Loading())
        }
        firestore.collection("products").whereEqualTo("category","Best Deals").get()
            .addOnSuccessListener { result ->
                val bestdealsProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestdealsProducts.emit(Resource.Success(bestdealsProductsList))
                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestdealsProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    fun fetchbestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("products").get()
            .addOnSuccessListener { result ->
                val bestProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductsList))
                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}