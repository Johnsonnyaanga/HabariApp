package com.example.habariapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.androiddevs.NewsApp.api.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article):Long
    @Query("SELECT * FROM articles_table")
    fun getAllArticles():LiveData<List<Article>>
    @Delete
    suspend fun deleteArticle(article: Article)
    @Query("DELETE FROM articles_table")
    suspend fun deleteAllCachedArticles()
}