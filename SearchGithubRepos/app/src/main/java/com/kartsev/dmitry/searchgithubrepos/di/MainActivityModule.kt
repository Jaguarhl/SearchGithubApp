package com.kartsev.dmitry.searchgithubrepos.di

import com.kartsev.dmitry.searchgithubrepos.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}