package com.example.gentlepark.data

data class User(
    val firstName:String,
    val lastName:String,
    val email:String,
    val imagepath:String=""
){
    constructor():this("","","","")
}
