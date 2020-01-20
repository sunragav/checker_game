package com.sunragav.idealo


import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.sunragav.boardview.domain.models.ErrorState
import com.sunragav.boardview.domain.models.FrameState
import com.sunragav.boardview.presentation.CheckerViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_checker.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class CheckerFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: CheckerViewModel.Factory
    private val viewModel by viewModels<CheckerViewModel>(factoryProducer = { viewModelFactory })
    private lateinit var btnControl: ImageButton
    val handler = Handler()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }


    private val timerTask = object : Runnable {
        override fun run() {
            viewModel.moveCommand.postValue(true)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checker, container, false)


        btnControl = view.btnControl

        btnControl.setOnClickListener {
            toggleBtn()
        }
        viewModel.checkerFrameLiveData.observe(this, Observer {
            when (val frameState = it.isValid(7, 3)) {
                is FrameState.Error -> when (frameState.error) {
                    is ErrorState.CoordinateOutOfBounds -> Toast.makeText(
                        context,
                        R.string.coordinate_out_of_bounds,
                        Toast.LENGTH_LONG
                    ).show()
                    is ErrorState.NoDrawableForCoin -> Toast.makeText(
                        context,
                        R.string.no_drawable_for_coin,
                        Toast.LENGTH_LONG
                    ).show()
                    is ErrorState.UnKnownState -> Toast.makeText(
                        context,
                        R.string.unknown_error,
                        Toast.LENGTH_LONG
                    ).show()
                    is ErrorState.EmptyInput -> Toast.makeText(
                        context,
                        R.string.input_missing,
                        Toast.LENGTH_LONG
                    ).show()
                    is ErrorState.EmptyMoves -> Toast.makeText(
                        context,
                        R.string.empty_input,
                        Toast.LENGTH_LONG
                    ).show()

                }
                is FrameState.Data -> {
                    if (frameState.frame?.index == 0 && view.checkerView.currentFrame != null) {
                        view.checkerView.rFrame = null
                        view.checkerView.gFrame = null
                        view.checkerView.bFrame = null
                        viewModel.clearFrames()
                        when (frameState.frame?.coin) {
                            0 -> viewModel.rFrame.value = frameState.frame
                            1 -> viewModel.gFrame.value = frameState.frame
                            2 -> viewModel.bFrame.value = frameState.frame
                        }
                    }

                    if(frameState.frame?.index == frameState.frame?.totalFrames?.minus(1))
                    {
                        Toast.makeText(
                            context,
                            R.string.end_of_the_movie,
                            Toast.LENGTH_LONG
                        ).show()
                        viewModel.playToggle.postValue(false)
                    }
                    view.checkerView.rFrame = viewModel.rFrame.value
                    view.checkerView.gFrame = viewModel.gFrame.value
                    view.checkerView.bFrame = viewModel.bFrame.value

                    view.checkerView.currentFrame = frameState.frame?.index
                }
            }

        })

        viewModel.playToggle.observe(this, Observer {
            if (it) {
                handler.postDelayed(timerTask, 0)
                btnControl.setImageResource(R.drawable.btn_pause)
            } else {
                handler.removeCallbacks(timerTask)
                btnControl.setImageResource(R.drawable.btn_play)
            }
        })

        return view
    }

    private fun toggleBtn() {
        viewModel.playToggle.postValue(viewModel.playToggle.value == false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(timerTask)
    }
}
