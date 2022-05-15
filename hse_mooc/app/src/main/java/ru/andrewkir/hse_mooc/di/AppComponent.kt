package com.example.digitalstudentassistant.di

import android.content.Context
import com.example.digitalstudentassistant.App
import com.example.digitalstudentassistant.MainActivity
import com.example.digitalstudentassistant.di.modules.DataModule
import com.example.digitalstudentassistant.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, NetworkModule::class])
interface AppComponent {
    fun provideCourseRepository(): CourseRepository

    fun  provideCoursesMainRepository(): CoursesMainRepository

    fun provideAuthRepository(): AuthRepository

    fun provideProfileRepository(): ProfileRepository

    fun provideCoursesSearchRepository():CoursesSearchRepository

    fun inject(app: App)

    fun inject(appActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun apiUrl(@Named(NetworkModule.BASE_URL) url: String): Builder

        @BindsInstance
        fun appContext(context: Context): Builder
    }
}