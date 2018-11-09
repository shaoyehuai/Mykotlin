package com.jsh.luo.mykotlin.view.banner.holder

import android.content.Context
import android.view.View

interface MyViewHolder<T>{
    /**
     * 创建View
     * @param context
     * @return
     */
    fun createView(context: Context) : View

    /**
     * 绑定数据
     * @param context
     * @param position
     * @param data
     */
    fun onBind(context: Context,position: Int,data: T)
}