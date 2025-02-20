package com.example.contenturl.service

import com.example.contenturl.model.Recipe
import com.example.contenturl.model.mockModel
import com.example.contenturl.repository.MockModelRepository
import com.example.contenturl.repository.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GptService @Autowired constructor(
    private val mockModelRepository: MockModelRepository,
    private val recipeRepository: RecipeRepository
) {

    private val restTemplate = RestTemplate()
//    private var apiKey: String? = null

    fun isRecipe(id: Long): String {
        val video: mockModel = mockModelRepository.findById(id).orElse(null) ?: return "Video not found."

//         Call ChatGPT for each question
//        val isRecipe = askChatGPT("Is this video a recipe? Title: ${video.title}. Description: ${video.description}.")
        val hasIngredients = askChatGPT("Does this video contain recipe ingredients? Description: ${video.description}.")
        val hasInstructions = askChatGPT("Does this video provide recipe instructions? Description: ${video.description}.")
        val hasName = askChatGPT("Does this video have a recipe name? Title: ${video.title}.")
        val hasSourceLink = askChatGPT("Does this video have a source link for the recipe? URL: ${video.url}.")
//        println(isRecipe);
//        println(hasIngredients)
//        println(hasInstructions)
//        println(hasName)
//        println(hasSourceLink)

        val conditions = listOf(hasIngredients, hasInstructions, hasName, hasSourceLink)

        return if (conditions.all { it }) {
            saveRecipe(video)
            "Video is a recipe."

        } else {
            "Video is not a recipe."
        }
    }
    

//    To save Receipe Post
    private fun saveRecipe(video: mockModel) {
        val recipe = Recipe(
            title = video.title,
            platformId=video.platformId,
            platformContentId=video.platformContentId,
            description = video.description,
            url = video.url,
            mediaUrl=video.mediaUrl,
            publishedAt = video.publishedAt

        )
        recipeRepository.save(recipe)
    }




    private fun askChatGPT(question: String): Boolean {
//        val apiKey=""
        val apiKey=System.getenv("API_KEY")

        val url = "https://api.openai.com/v1/chat/completions"

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", "Bearer $apiKey")
        }
        val requestBody = mapOf(
            "model" to "gpt-4-turbo",
            "messages" to listOf(mapOf("role" to "user", "content" to question)),
            "max_tokens" to 50
        )

        val requestEntity = HttpEntity(requestBody, headers)

        return try {
            val response: ResponseEntity<Map<String, Any>> = restTemplate.postForEntity(url, requestEntity, Map::class.java) as ResponseEntity<Map<String, Any>>
            val choices = response.body?.get("choices") as? List<Map<String, Any>>
            val answer = choices?.firstOrNull()?.get("message")?.let { it as? Map<String, Any> }?.get("content") as? String

            answer?.trim()?.lowercase()?.contains("yes") == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
