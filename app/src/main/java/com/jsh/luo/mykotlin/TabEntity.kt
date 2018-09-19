package com.jsh.luo.mykotlin

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity constructor(title:String,selectedIcon:Int,unSelectedIcon : Int) : CustomTabEntity{

    private var mTitle : String = title

    private var mSelectedIcon : Int = selectedIcon

    private var mUnSelectedIcon = unSelectedIcon

    override fun getTabUnselectedIcon() = mUnSelectedIcon

    override fun getTabSelectedIcon() = mSelectedIcon

    override fun getTabTitle() = mTitle
}