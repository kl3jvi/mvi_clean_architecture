package com.kl3jvi.persistence.di

import android.app.Application
import androidx.room.Room
import com.kl3jvi.persistence.dao.RestaurantDao
import com.kl3jvi.persistence.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PersistenceModule {
    private const val DATABASE_NAME = "take_away.db"

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
    ): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java, DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRestaurantDao(appDatabase: AppDatabase): RestaurantDao = appDatabase.restaurantDao()

}