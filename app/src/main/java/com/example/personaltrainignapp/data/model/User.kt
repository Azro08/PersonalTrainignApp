package com.example.personaltrainignapp.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val role: String = "",
    val fullName: String = "",
    val imageUrl: String = "",
    val phoneNumber: String = "",
    val address: String = "",
)
//
//fun User.toUsersDomain() = User(
//    id = id,
//    email = email,
//    role = role,
//    fullName = fullName,
//    imageUrl = imageUrl,
//    phoneNumber = phoneNumber,
//    address = address,
//)