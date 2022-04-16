package com.nazirman.sub1storyapp.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResult(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("token")
    val token: String,
    @Expose
    @SerializedName("userId")
    val userId: String
)