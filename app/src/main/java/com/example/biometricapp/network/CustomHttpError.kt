package com.example.biometricapp.network

import com.google.gson.annotations.SerializedName

data class CustomHttpError(

    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String


)
