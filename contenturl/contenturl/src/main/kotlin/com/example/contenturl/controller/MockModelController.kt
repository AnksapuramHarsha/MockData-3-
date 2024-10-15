package com.example.contenturl.controller
import com.example.contenturl.service.MockModelService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity

@RestController
class MockModelController(private val mockModelService: MockModelService) {

    @PostMapping("/api/content/save")
    fun saveContent(): ResponseEntity<String> {
        return try {
            val resultMessage = mockModelService.saveContentFromResponse()
            ResponseEntity.ok(resultMessage)
        } catch (e: Exception) {
            // Log the exception (you can use a logging framework)
            println("Error saving content: ${e.message}")
            ResponseEntity.status(500).body("Failed to save content: ${e.message}")
        }
    }
}

