package com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Di

import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Data.Repository.ProjectRepositoryImpl
import com.m1guelgtz.templatecarsapi.Demo.Features.Projects.Domain.Repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProjectsModule {

    @Provides
    @Singleton
    fun provideProjectRepository(api: DevSyncApi): ProjectRepository {
        return ProjectRepositoryImpl(api)
    }
}
