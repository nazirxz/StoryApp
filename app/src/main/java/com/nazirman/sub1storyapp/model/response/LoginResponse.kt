package com.nazirman.sub1storyapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("error")
    val error: Boolean,
    @Expose
    @SerializedName("loginResult")
    val loginResult: LoginResult,
    @Expose
    @SerializedName("message")
    val message: String
)