package com.example.mangashelfassignment.di

import com.example.mangashelfassignment.data.remote.RemoteDataSource
import com.example.mangashelfassignment.domain.DataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(dataSource: RemoteDataSource): DataSource

}