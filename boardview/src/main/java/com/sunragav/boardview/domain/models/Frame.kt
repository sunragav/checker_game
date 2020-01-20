package com.sunragav.boardview.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Frame(
    val index:Int,
    val totalFrames:Int,
    val coin:Int,
    val x:Int,
    val y:Int
):Parcelable