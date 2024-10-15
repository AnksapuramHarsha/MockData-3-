package com.example.contenturl.controller

import com.example.contenturl.service.GptService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/videos")
class GptController @Autowired constructor(
    private val gptService: GptService
) {

    @GetMapping("/check-recipe/{id}")
    fun checkRecipe(@PathVariable id: Long): String {
        return gptService.isRecipe(id)
    }
}
