package com.sunragav.boardview.data

import com.sunragav.boardview.data.local.LocalFrameRepository
import com.sunragav.boardview.domain.models.FrameState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FrameRepositoryImpl @Inject constructor(private val localRepository: LocalFrameRepository) : FrameRepository{
    override suspend fun getNextFrame(index:Int): FrameState = withContext(Dispatchers.IO){
        localRepository.getNextFrame(index)
    }
}