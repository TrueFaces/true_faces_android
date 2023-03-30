package com.n1rocket.truefaces.di

import android.content.Context
import com.n1rocket.truefaces.datasources.IPreferencesDataSource
import com.n1rocket.truefaces.datasources.IRestDataSource
import com.n1rocket.truefaces.datasources.PreferencesDataSource
import com.n1rocket.truefaces.datasources.RestDataSource
import com.n1rocket.truefaces.managers.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Singleton
    @Provides
    fun serverDataSource(): IRestDataSource =
        RestDataSource("https://truefaces-api-ygmgobsv6q-ew.a.run.app")

    @Singleton
    @Provides
    fun preferencesDataSource(@ApplicationContext appContext: Context): IPreferencesDataSource =
        PreferencesDataSource(PrefManager(appContext))
}








