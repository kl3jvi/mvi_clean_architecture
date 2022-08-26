package com.kl3jvi.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.database.AppDatabase.Companion.DATABASE_VERSION
import com.kl3jvi.persistence.models.RestaurantEntity

@Database(
    entities = [
        RestaurantEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        const val DATABASE_VERSION = 1
    }
}