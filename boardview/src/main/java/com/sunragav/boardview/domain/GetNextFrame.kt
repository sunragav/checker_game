package com.sunragav.boardview.domain

import com.sunragav.boardview.data.FrameRepository
import javax.inject.Inject

class GetNextFrame @Inject constructor(private val frameRepository: FrameRepository){
    suspend operator fun invoke(index:Int) = frameRepository.getNextFrame(index)
}