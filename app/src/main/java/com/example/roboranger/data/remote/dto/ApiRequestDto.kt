package com.example.roboranger.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthRequestDto(
    @SerializedName("user_email")
    val userEmail: String,
    @SerializedName("password")
    val password: String
)

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("user_email")
    val userEmail: String,
    @SerializedName("tenant")
    val tenant: String
)

data class AuthResponseDto(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("token")
    val token: String,
    @SerializedName("message")
    val message: String?,
    @SerializedName("token_type")
    val tokenType: String?,
    @SerializedName("expires_in")
    val expiresIn: Long?
)
