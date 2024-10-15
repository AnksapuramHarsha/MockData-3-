package com.example.contenturl.repository

import com.example.contenturl.model.mockModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MockModelRepository:JpaRepository<mockModel,Long> {
    fun findByPlatformContentId(platformContentId:String):mockModel?
}