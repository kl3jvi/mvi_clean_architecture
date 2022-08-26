package com.kl3jvi.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val appDispatchers: AppDispatchers)

enum class AppDispatchers {
    DEFAULT,
    IO,
    MAIN,
}



