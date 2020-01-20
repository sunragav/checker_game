package com.sunragav.boardview.data.local.json

import com.google.gson.Gson
import com.sunragav.boardview.data.local.LocalFrameRepository
import com.sunragav.boardview.data.local.json.mapper.LocalToDomainMapper
import com.sunragav.boardview.data.local.json.models.FrameWrapper
import com.sunragav.boardview.domain.models.ErrorState.EmptyInput
import com.sunragav.boardview.domain.models.ErrorState.EmptyMoves
import com.sunragav.boardview.domain.models.FrameState
import com.sunragav.boardview.domain.models.FrameState.*


class JSONFrameRepository(json: String, private val mapper: LocalToDomainMapper) :
    LocalFrameRepository {
    private var frameWrapper: FrameWrapper? = null

    init {
        frameWrapper = Gson().fromJson(json, FrameWrapper::class.java)
    }

    override suspend fun getNextFrame(index: Int): FrameState {
        var i = index
        return frameWrapper?.let { frameWrapper ->
            frameWrapper.moves?.let {
                if (index == it.size) i -= 1
                Data(mapper.transform(i, it[i], it.size))
            } ?: Error(EmptyMoves)
        } ?: Error(EmptyInput)
    }

}