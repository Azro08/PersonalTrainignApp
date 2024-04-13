package com.example.personaltrainignapp.di

import android.content.Context
import com.example.personaltrainignapp.util.AuthManager
import com.example.personaltrainignapp.util.LangUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    @Provides
    @Singleton
    fun provideAuthManager(@ApplicationContext context: Context): AuthManager =
        AuthManager(context)

    @Provides
    @Singleton
    fun provideLanguageUtils(@ApplicationContext context: Context): LangUtils =
        LangUtils(context)

}