package com.example.contenturl.model

import jakarta.persistence.*


@Entity
data class Recipe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val platformId: String,
    val title: String,
    val description: String,
    val platformContentId: String,
    val url: String,
    val mediaUrl: String,
    val publishedAt: String

)
