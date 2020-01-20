package com.sunragav.boardview.data.local

import com.sunragav.boardview.domain.models.FrameState

interface LocalFrameRepository{
    suspend fun getNextFrame(index:Int): FrameState
}