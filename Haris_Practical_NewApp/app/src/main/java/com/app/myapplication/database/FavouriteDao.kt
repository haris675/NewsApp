package com.app.myapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.myapplication.model.ArticlesItem

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(item: ArticlesItem)

    @Query("SELECT * FROM tblfavourite")
    suspend fun getAllFavouriteNews(): List<ArticlesItem>

    @Query("SELECT * FROM tblfavourite WHERE publishedAt = :query")
    suspend fun search(query: String): List<ArticlesItem>

    @Query("DELETE FROM tblfavourite WHERE publishedAt = :query")
    suspend fun deleteById(query: String)

}