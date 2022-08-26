package com.kl3jvi.common.di

import com.kl3jvi.common.AppDispatchers.DEFAULT
import com.kl3jvi.common.AppDispatchers.IO
import com.kl3jvi.common.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/* A module that provides a singleton instance of a CoroutineDispatcher. */
@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(DEFAULT)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
