package com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Di

import com.m1guelgtz.templatecarsapi.Demo.Core.Di.DevSyncRetrofit
import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Core.SessionManager
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Data.Repository.AuthRepositoryImpl
import com.m1guelgtz.templatecarsapi.Demo.Features.Auth.Domain.Repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideDevSyncApi(@DevSyncRetrofit retrofit: Retrofit): DevSyncApi {
        return retrofit.create(DevSyncApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: DevSyncApi, sessionManager: SessionManager): AuthRepository {
        return AuthRepositoryImpl(api, sessionManager)
    }
}
