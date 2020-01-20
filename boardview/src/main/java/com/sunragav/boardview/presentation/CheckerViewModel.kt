package com.sunragav.boardview.presentation

import androidx.lifecycle.*
import com.sunragav.boardview.domain.GetNextFrame
import com.sunragav.boardview.domain.models.Frame
import com.sunragav.boardview.domain.models.FrameState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckerViewModel @Inject constructor(private val getNextFrame: GetNextFrame) : ViewModel() {
    fun clearFrames() {
        rFrame.value = null
        gFrame.value = null
        bFrame.value = null
    }

    val uiState: LiveData<UiState>
        get() = _uiState
    val checkerFrameLiveData: LiveData<FrameState>
        get() = _checkerFrameLiveData


    val moveCommand = MutableLiveData<Boolean>()

    val playToggle = MutableLiveData<Boolean>(false)

    private val currentFrame = MutableLiveData(0)
    val rFrame = MutableLiveData<Frame>()
    val gFrame = MutableLiveData<Frame>()
    val bFrame = MutableLiveData<Frame>()
    //Private
    private val _uiState = MutableLiveData<UiState>()
    private val _checkerFrameLiveData = MediatorLiveData<FrameState>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.postValue(UiState.Error(throwable))
    }


    //init
    init {
        _checkerFrameLiveData.addSource(moveCommand) {
            if (playToggle.value == true) {
                viewModelScope.launch(exceptionHandler) {
                    _uiState.postValue(UiState.Loading)
                    _checkerFrameLiveData.postValue(
                        getNextFrame(currentFrame.value!!).also {
                            if (it !is FrameState.Error) {
                                when(it.frame?.coin){
                                    0-> rFrame.postValue(it.frame)
                                    1-> gFrame.postValue(it.frame)
                                    2-> bFrame.postValue(it.frame)
                                }
                                currentFrame.postValue(it.frame?.index?.plus(1))
                            }
                            if (it.frame?.index == it.frame?.totalFrames?.minus(1)) {
                                currentFrame.postValue(0)
                            }
                        })
                    _uiState.postValue(UiState.Complete)
                }
            }
        }
    }


    /////////////////////////////////////////////////////////////////////
    //ViewModelFactory

    class Factory(
        private val getNextFrame: GetNextFrame
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CheckerViewModel::class.java)) {
                return CheckerViewModel(getNextFrame) as T
            }
            throw IllegalArgumentException("ViewModel not found.")
        }
    }

    /////////////////////////////////////////////////////////////////////
}