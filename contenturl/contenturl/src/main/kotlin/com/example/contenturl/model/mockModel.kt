package com.example.contenturl.model

import jakarta.persistence.*

@Entity
class mockModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val platformId: String,
    val platformName: String,
    val platformLogoUrl: String,
    val engagementLikes: Int,
    val engagementComments: Int,
    val engagementViews: Int,
    val engagementShares: Int,
    val platformContentId: String,
    val title: String,
    val format: String,
    val type: String,
    val url: String,
    val mediaUrl: String,
    val duration: Int,
    val description: String,
    val thumbnailUrl: String,
    val publishedAt: String
) {
}