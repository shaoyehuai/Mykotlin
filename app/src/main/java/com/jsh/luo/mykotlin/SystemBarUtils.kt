package com.jsh.luo.mykotlin

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

/**
 * 沉浸式主题
 * Created by luo on 2018/9/18.
 * */

class SystemBarUtils {

    companion object {

        /**
         * 设置页面最外层布局 FitsSystemWindows 属性设置页面最外层布局 FitsSystemWindows 属性
         *
         * @param activity 需要设置的对象
         * @param value 是否需要设置
         * */
        fun  setFitsSystemWindows(activity: Activity,value: Boolean) {
            val contentFragment : ViewGroup = activity.findViewById(android.R.id.content)

            val rootView : View? = contentFragment.getChildAt(0)
            if(Build.VERSION.SDK_INT >= 14){
                rootView?.fitsSystemWindows = value
            }
        }
    }

    private fun getStatusBarHeight(activity: Activity) : Int{
        var result = 0
        //获取状态栏高度的资源id
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        if(resourceId > 0){
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun addStatusViewWithColor(activity: Activity, color : Int){
        val contentView : ViewGroup = activity.findViewById(android.R.id.content)

        val statusBarView = View(activity)
        val lp : LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity))
        statusBarView.setBackgroundColor(color)
        statusBarView.layoutParams = lp
        contentView.addView(statusBarView,0)
    }
}