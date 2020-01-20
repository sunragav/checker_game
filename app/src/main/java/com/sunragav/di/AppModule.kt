package com.sunragav.di

import android.app.Application
import android.content.Context
import com.sunragav.idealo.CheckerFragment
import com.sunragav.idealo.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {
    @Binds
    abstract fun bindContext(application: Application): Context

    @ContributesAndroidInjector
    internal abstract fun contributesMainActivity(): MainActivity


    @ContributesAndroidInjector
    internal abstract fun contributesCheckerFragment(): CheckerFragment
}