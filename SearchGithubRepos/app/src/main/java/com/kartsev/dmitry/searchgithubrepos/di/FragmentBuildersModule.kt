package com.kartsev.dmitry.searchgithubrepos.di

import com.kartsev.dmitry.searchgithubrepos.presentation.search.SearchRepoFragment
import com.kartsev.dmitry.searchgithubrepos.presentation.details.RepoDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeSearchRepoFragment(): SearchRepoFragment

    @ContributesAndroidInjector
    abstract fun contributeUserInfoFragment(): RepoDetailsFragment
}