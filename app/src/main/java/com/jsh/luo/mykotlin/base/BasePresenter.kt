package com.jsh.luo.mykotlin.base


abstract class BasePresenter<V>{
    protected var mView: V? = null

//    protected var mCompositeSubscription = CompositeDisposable()

    fun setView(v: V) {
        this.mView = v
        this.onAttached()
    }

    abstract fun onAttached()

    fun onDetached() {
//        mCompositeSubscription.dispose()
    }
}
