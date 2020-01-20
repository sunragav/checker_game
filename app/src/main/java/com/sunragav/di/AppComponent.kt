package com.sunragav.di

import android.app.Application
import com.sunragav.app.CheckerApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DataModule::class,
        PresentationModule::class,
        AppModule::class
    ]
)
interface AppComponent : AndroidInjector<CheckerApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: CheckerApp)
}