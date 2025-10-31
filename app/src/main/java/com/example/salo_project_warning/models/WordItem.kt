package com.example.salo_project_warning.models

import kotlinx.serialization.Serializable

@Serializable
data class WordItem(
    val text: String,
    val imageUri: String
)
