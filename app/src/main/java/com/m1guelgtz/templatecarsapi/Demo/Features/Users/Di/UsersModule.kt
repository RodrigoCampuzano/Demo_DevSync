package com.m1guelgtz.templatecarsapi.Demo.Features.Users.Di

import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Data.Repository.UserRepositoryImpl
import com.m1guelgtz.templatecarsapi.Demo.Features.Users.Domain.Repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideUserRepository(api: DevSyncApi): UserRepository {
        return UserRepositoryImpl(api)
    }
}
