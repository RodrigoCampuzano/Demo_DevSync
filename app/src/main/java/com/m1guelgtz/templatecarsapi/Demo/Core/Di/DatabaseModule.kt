package com.m1guelgtz.templatecarsapi.Demo.Core.Di

import android.content.Context
import androidx.room.Room
import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.AppDatabase
import com.m1guelgtz.templatecarsapi.Demo.Core.Data.Local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "devsync_db"
        )
        .fallbackToDestructiveMigration() // Critical to handle schema changes
        .build()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }
}
