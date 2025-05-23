package com.example.compose

data class ChatMessage(
    val id: Int,
    val avatar: String,
    val nickname: String,
    val content: String
)