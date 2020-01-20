package com.sunragav.boardview.domain.models

import com.sunragav.boardview.domain.models.ErrorState.CoordinateOutOfBounds
import com.sunragav.boardview.domain.models.ErrorState.NoDrawableForCoin


sealed class FrameState( val frame: Frame?) {
    class Data(frame: Frame) : FrameState(frame)
    class Error(val error: ErrorState) : FrameState(null)

    fun isValid(size: Int, coins: Int) = frame?.let {
        when {
            it.coin > coins - 1 || it.coin < 0 -> Error(NoDrawableForCoin)
            it.x > size - 1 || it.y > size - 1 || it.x < 0 || it.y < 0 -> Error(
                CoordinateOutOfBounds
            )
            else -> this
        }
    }
}