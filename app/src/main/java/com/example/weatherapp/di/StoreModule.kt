package com.example.weatherapp.di

import com.example.weatherapp.redux.Action
import com.example.weatherapp.redux.AppState
import com.example.weatherapp.redux.appReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore
import org.reduxkotlin.middleware
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    @Singleton
    fun provideStore(): Store<AppState> {
        val loggingMiddleware = middleware<AppState> { store, next, action ->
            println("Dispatching action: $action")
            val result = next(action)
            println("New state: ${store.state}")
            result
        }

        return createThreadSafeStore(
            reducer = { state: AppState, action: Any ->
                if (action is Action) {
                    appReducer(state, action)
                } else {
                    state
                }
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(loggingMiddleware)
        )
    }
}