package com.jsh.luo.mykotlin.base

import android.app.Activity
import android.os.Bundle
import com.jsh.luo.mykotlin.utils.InstanceUtil
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<P : BasePresenter<*>> : Activity() {

    protected var mPresenter: P? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
        setContentView(getLayoutRes())
    }

    @Suppress("UNCHECKED_CAST")
    protected fun initPresenter() {
        if (this is BaseView && this.javaClass.genericSuperclass is ParameterizedType
                && (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.isNotEmpty()) {
            val mPresenterClass = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<P>
            mPresenter = InstanceUtil.getInstance(mPresenterClass)
        }
    }

    abstract fun getLayoutRes() : Int

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.onDetached()
    }
}
