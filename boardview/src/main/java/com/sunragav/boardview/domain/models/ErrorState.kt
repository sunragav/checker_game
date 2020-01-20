package com.sunragav.boardview.domain.models

sealed class ErrorState {
    object NoDrawableForCoin : ErrorState()
    object CoordinateOutOfBounds : ErrorState()
    object UnKnownState : ErrorState()
    object EmptyMoves : ErrorState()
    object EmptyInput : ErrorState()
}