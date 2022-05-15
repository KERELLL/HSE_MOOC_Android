package com.example.digitalstudentassistant.di.modules

import com.example.digitalstudentassistant.data.repositories.AuthRepositoryImpl
import com.example.digitalstudentassistant.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun  provideCourseRepository(courseRepository: CourseRepositoryImpl): CourseRepository

    @Binds
    @Singleton
    abstract fun  provideCoursesMainRepository(coursesMainRepository: CoursesMainRepositoryImpl): CoursesMainRepository

    @Binds
    @Singleton
    abstract fun  provideProfileRepository(profileRepository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun  provideCoursesSearchRepository(coursesSearchRepository: CoursesSearchRepositoryImpl): CoursesSearchRepository
}