package com.sunragav.boardview.data.local.json.mapper

import com.sunragav.boardview.data.local.json.models.JSONFrame
import com.sunragav.boardview.domain.models.Frame
import javax.inject.Inject

class LocalToDomainMapper @Inject constructor() {
    fun transform(index: Int, jsonFrame: JSONFrame, totalFrames:Int): Frame {
        return Frame(
            index = index,
            totalFrames = totalFrames,
            coin = jsonFrame.p,
            x = jsonFrame.x,
            y = jsonFrame.y
        )
    }
}