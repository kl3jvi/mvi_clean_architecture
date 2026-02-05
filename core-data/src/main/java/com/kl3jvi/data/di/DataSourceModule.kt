package com.kl3jvi.data.di

import android.content.Context
import android.content.res.AssetManager
import com.kl3jvi.data.datasource.RestaurantDataSource
import com.kl3jvi.data.datasource.StaticRestaurantDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import java.io.InputStream
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsRestaurantDataSource(
        staticDataSource: StaticRestaurantDataSource
    ): RestaurantDataSource

    /* Providing a singleton instance of Json. */
    companion object {
        @Provides
        @Singleton
        fun providesNetworkJson(): Json = Json { ignoreUnknownKeys = true }

        @Provides
        @Singleton
        fun provideInputStream(
            @ApplicationContext context: Context
        ): InputStream {
            return context.assets.open(
                "sample_data.json",
                AssetManager.ACCESS_BUFFER
            )
        }
    }
}
