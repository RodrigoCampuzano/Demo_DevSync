package com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Di

import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.TaskDao
import com.m1guelgtz.templatecarsapi.Demo.Core.Network.DevSyncApi
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Data.Repository.TaskRepositoryImpl
import com.m1guelgtz.templatecarsapi.Demo.Features.Tasks.Domain.Repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TasksModule {

    @Provides
    @Singleton
    fun provideTaskRepository(api: DevSyncApi, taskDao: TaskDao): TaskRepository {
        return TaskRepositoryImpl(api, taskDao)
    }
}
