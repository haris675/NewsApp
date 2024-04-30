package com.app.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.myapplication.model.ArticlesItem

@Database(entities = [ArticlesItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
}