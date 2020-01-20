package com.sunragav.di

import com.sunragav.boardview.domain.GetNextFrame
import com.sunragav.boardview.presentation.CheckerViewModel
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {
    @Provides
    fun provideViewModelFactory(
        getCatalog: GetNextFrame
    ) = CheckerViewModel.Factory(getCatalog)
}