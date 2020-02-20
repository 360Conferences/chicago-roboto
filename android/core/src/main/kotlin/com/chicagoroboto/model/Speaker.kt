package com.chicagoroboto.model

data class Speaker(
    val id: String,
    val name: String = "",
    val title: String = "",
    val company: String = "",
    val email: String = "",
    val twitter: String = "",
    val github: String = "",
    val bio: String = "",
    val avatarUrl: String = ""
)
