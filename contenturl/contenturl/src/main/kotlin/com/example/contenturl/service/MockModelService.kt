package com.example.contenturl.service

import com.example.contenturl.model.mockModel
import com.example.contenturl.repository.MockModelRepository
import org.springframework.beans.factory.annotation.Autowired
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class MockModelService @Autowired constructor(
    private val mockModelRepository: MockModelRepository
) {
    fun saveContentFromResponse(): String {
        val jsonResponse = """
        {
            "data": [
                {
                    "work_platform": {
                        "id": "12345",
                        "name": "Instagram",
                        "logo_url": "https://example.com/logo.png"
                    },
                    "engagement": {
                        "like_count": 100,
                        "comment_count": 25,
                        "view_count": 1000,
                        "share_count": 10
                    },
                    "platform_content_id": "content_1",
                    "title": "My First Post",
                    "format": "image",
                    "type": "photo",
                    "url": "https://example.com/post1",
                    "media_url": "https://example.com/media1.png",
                    "duration": 0,
                    "description": "This is my first post!",
                    "thumbnail_url": "https://example.com/thumbnail1.png",
                    "published_at": "2024-01-01T00:00:00Z"
                },
                {
                    "work_platform": {
                        "id": "12345",
                        "name": "Instagram",
                        "logo_url": "https://insightiq.ai/storage/instagram.png"
                    },
                    "engagement": {
                        "like_count": 42134,
                        "comment_count": 4234,
                        "view_count": 42343,
                        "share_count": 42343
                    },
                    "platform_content_id": "12ccaf87",
                    "title": "USA Tours",
                    "format": "VIDEO",
                    "type": "VIDEO",
                    "url": "https://youtu.be/jzWweY4xPe8",
                    "media_url": "https://example.com/media2",
                    "duration": 523,
                    "description": "US tours video.",
                    "thumbnail_url": "https://youtu.be/jzWweY4xPe8.png",
                    "published_at": "2021-05-26T00:00:00.000000"
                },
                {
                    "work_platform": {
                        "id": "12345",
                        "name": "Instagram",
                        "logo_url": "https://insightiq.ai/storage/instagram.png"
                    },
                    "engagement": {
                        "like_count": 100,
                        "comment_count": 100,
                        "view_count": 100,
                        "share_count": 100
                    },
                    "platform_content_id": "1000",
                    "title": "Easy Spaghetti Carbonara",
                    "format": "VIDEO",
                    "type": "VIDEO",
                    "url": "https://www.youtube.com/shorts/MXiyjBP-aBk",
                    "media_url": "https://video.example.com/spaghetti_carbonara.mp4",
                    "duration": 240,
                    "description": "Learn how to make Easy Spaghetti Carbonara! Ingredients: Spaghetti, Eggs, Parmesan cheese, Bacon, Salt, Pepper. Instructions: Cook spaghetti. Mix eggs and cheese. Fry bacon. Combine everything and serve hot!",
                    "thumbnail_url": "https://example.com/spaghetti-carbonara-thumbnail.jpg",
                    "published_at": "2023-08-26T00:00:00.000000"
                }

            ]
        }
        """.trimIndent()

        val mapper = ObjectMapper()
        val jsonNode = mapper.readTree(jsonResponse)

        val dataNode = jsonNode.get("data")

        if (dataNode.isArray && dataNode.size() > 0) {
            val messages = mutableListOf<String>()

            for (i in 0 until dataNode.size()) {
                val contentNode = dataNode[i]
                val platformContentId = contentNode.get("platform_content_id").asText()

                val existingContent = mockModelRepository.findByPlatformContentId(platformContentId)

                if (existingContent == null) {
                    val mockModelContent = mockModel(
                        platformId = contentNode.get("work_platform").get("id").asText(),
                        platformName = contentNode.get("work_platform").get("name").asText(),
                        platformLogoUrl = contentNode.get("work_platform").get("logo_url").asText(),
                        engagementLikes = contentNode.get("engagement").get("like_count").asInt(),
                        engagementComments = contentNode.get("engagement").get("comment_count").asInt(),
                        engagementViews = contentNode.get("engagement").get("view_count").asInt(),
                        engagementShares = contentNode.get("engagement").get("share_count").asInt(),
                        platformContentId = platformContentId,
                        title = contentNode.get("title").asText(),
                        format = contentNode.get("format").asText(),
                        type = contentNode.get("type").asText(),
                        url = contentNode.get("url").asText(),
                        mediaUrl = contentNode.get("media_url").asText(),
                        duration = contentNode.get("duration").asInt(),
                        description = contentNode.get("description").asText(),
                        thumbnailUrl = contentNode.get("thumbnail_url").asText(),
                        publishedAt = contentNode.get("published_at").asText()
                    )

                    mockModelRepository.save(mockModelContent)
                    messages.add("Saved content with platform_content_id: $platformContentId")
                } else {
                    messages.add("Content with platform_content_id '$platformContentId' already exists.")
                }
            }

            return messages.joinToString("; ")
        } else {
            return "No data available in response."
        }
    }
}
