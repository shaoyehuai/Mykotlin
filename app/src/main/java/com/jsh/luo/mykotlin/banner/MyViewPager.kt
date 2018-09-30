package com.jsh.luo.mykotlin.banner

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.util.SparseIntArray
import java.util.*


class MyViewPager(context: Context,attrs : AttributeSet?) : ViewPager(context,attrs){

    private val childCenterXAbs = ArrayList<Int>()
    private val childIndex = SparseIntArray()

    constructor(context: Context) : this(context,null)

    init {
        this.clipToPadding = false
        this.overScrollMode = View.OVER_SCROLL_NEVER
    }

    override fun getChildDrawingOrder(childCount: Int, n: Int): Int {
        if(n == 0 || childIndex.size() != childCount){
            childCenterXAbs.clear()
            childIndex.clear()
            val viewCenterX = getViewCenterX(this)
            for(j in 0 until childCount){
                var indexAbs = Math.abs(viewCenterX - getViewCenterX(getChildAt(j)))
                if(childIndex.size() <= indexAbs){
                    ++indexAbs
                }
                childCenterXAbs.add(indexAbs)
                childIndex.append(indexAbs, n)
            }
            childCenterXAbs.sort() //1,0,2  0,1,2
        }
        //那个item距离中心点远一些，就先draw它。（最近的就是中间放大的item,最后draw）
        return childIndex.get(childCenterXAbs[childCount - 1 - n])
    }

    private fun getViewCenterX(view : View) : Int{
        val array = IntArray(2)
        view.getLocationOnScreen(array)
        return array[0] + view.width / 2
    }
}