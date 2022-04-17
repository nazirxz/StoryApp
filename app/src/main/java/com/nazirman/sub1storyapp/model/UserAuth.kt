package com.nazirman.sub1storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAuth(
    val token: String,
    val isLogin: Boolean
) : Parcelable
