package com.sunragav.boardview.data

import com.sunragav.boardview.domain.models.FrameState

interface FrameRepository{
    suspend fun getNextFrame(index:Int): FrameState
}