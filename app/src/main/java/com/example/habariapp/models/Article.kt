package com.androiddevs.NewsApp.api.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles_table"
)
data class Article(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
):Serializable