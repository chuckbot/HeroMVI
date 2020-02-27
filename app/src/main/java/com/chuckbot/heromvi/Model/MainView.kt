package com.chuckbot.heromvi.Model

import com.chuckbot.heromvi.View.MainViewState
import com.hannesdorfmann.mosby3.mvp.MvpView
import io.reactivex.Observable

interface MainView : MvpView {
    val imageIntent:Observable<Int> // imageIntent will integer index to get image from image list

    fun render(viewState: MainViewState)
}