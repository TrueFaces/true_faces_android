package com.n1rocket.truefaces.di

import com.n1rocket.truefaces.datasources.IPreferencesDataSource
import com.n1rocket.truefaces.datasources.IRestDataSource
import com.n1rocket.truefaces.repository.IRepository
import com.n1rocket.truefaces.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun repository(restDataSource: IRestDataSource, preferencesDataSource: IPreferencesDataSource): IRepository {
        return Repository(restDataSource, preferencesDataSource)
    }
}








