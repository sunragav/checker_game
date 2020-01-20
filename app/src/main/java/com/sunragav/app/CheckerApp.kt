package com.sunragav.app

import com.sunragav.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class CheckerApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}