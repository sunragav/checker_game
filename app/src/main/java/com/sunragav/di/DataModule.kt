package com.sunragav.di

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import com.sunragav.boardview.data.FrameRepository
import com.sunragav.boardview.data.FrameRepositoryImpl
import com.sunragav.boardview.data.local.LocalFrameRepository
import com.sunragav.boardview.data.local.json.JSONFrameRepository
import com.sunragav.boardview.data.local.json.mapper.LocalToDomainMapper
import com.sunragav.idealo.R
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DataModule.Binders::class])
class DataModule {

    @Module
    interface Binders {
        @Binds
        fun bindsFrameRepository(
            repoImpl: FrameRepositoryImpl
        ): FrameRepository

        @Binds
        fun bindsLocalFrameRepository(
            localFrameRepo: JSONFrameRepository
        ): LocalFrameRepository

    }

    @Singleton
    @Provides
    fun providesJSONFrameRepository(
        context: Context
    ):JSONFrameRepository{
        val moves = context.resources.getRawTextFile(R.raw.play)
        return JSONFrameRepository(moves, LocalToDomainMapper())
    }


    private fun Resources.getRawTextFile(@RawRes id: Int) =
        openRawResource(id).bufferedReader().use { it.readText() }
}