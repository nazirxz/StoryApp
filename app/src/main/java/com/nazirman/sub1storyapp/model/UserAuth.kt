package com.nazirman.sub1storyapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserAuth(
    val token: String,
    val isLogin: Boolean
) : Parcelable
