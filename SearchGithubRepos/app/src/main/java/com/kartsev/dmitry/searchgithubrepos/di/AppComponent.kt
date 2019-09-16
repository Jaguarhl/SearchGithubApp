package com.kartsev.dmitry.searchgithubrepos.di

import android.app.Application
import com.kartsev.dmitry.searchgithubrepos.GithubApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, NetworkModule::class, StorageModule::class,
    RepositoryModule::class, ViewModelModule::class, MainActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(githubApp: GithubApplication)
}