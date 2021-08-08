package com.example.habariapp.di

import android.app.Activity
import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesApplication(
        @ApplicationContext activity: Activity
    ):Application{
        return  activity.application
    }
}