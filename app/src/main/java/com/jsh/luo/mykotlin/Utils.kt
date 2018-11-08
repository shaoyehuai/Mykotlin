package com.jsh.luo.mykotlin

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue


object Utils {

    /**
     * dp to px
     * @param dp 传入的dp
     * */
    fun dp2px(dp : Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp.toFloat(),Resources.getSystem().displayMetrics)


    fun getScreenWidth(context: Context): Int {
        val resources = context.resources
        val dm = resources.displayMetrics
        return dm.widthPixels
    }
}