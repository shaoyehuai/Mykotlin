package com.jsh.luo.mykotlin.view.banner.holder

interface MyHolderCreate<VH : MyViewHolder<*>> {

    /**
     * 创建ViewHolder
     * @return
     */
    fun createViewHolder() : VH
}