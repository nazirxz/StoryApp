package com.nazirman.sub1storyapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    var name: String? = null,
    var photo: String? = null,
    var description: String? = null
) : Parcelable
