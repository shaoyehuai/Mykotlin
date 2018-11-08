package com.jsh.luo.mykotlin.view.banner.transformer

import android.support.v4.view.ViewPager
import android.view.View

class CoverModeTransformer(pager: ViewPager) : ViewPager.PageTransformer{

    private var reduceX = 0.0f
    private var itemWidth = 0f
    private var offsetPosition = 0f
    private val mCoverWidth: Int = 0
    private val mScaleMax = 1.0f
    private val mScaleMin = 0.9f
    private var mViewPager: ViewPager = pager

    override fun transformPage(page: View, position: Float) {
        if(offsetPosition == 0f){
            val paddingLeft = mViewPager.paddingLeft
            val paddingRight = mViewPager.paddingRight
            val width = mViewPager.measuredWidth
            offsetPosition = (paddingLeft / (width - paddingLeft - paddingRight)).toFloat()
        }

        val currentPos = position - offsetPosition
        if(itemWidth == 0f){
            itemWidth = page.width.toFloat()
            reduceX = (2.0f - mScaleMax - mScaleMin) * itemWidth / 2.0f
        }

        when {
            currentPos <= -1.0f -> {
                page.translationX = reduceX + mCoverWidth
                page.scaleX = mScaleMin
                page.scaleY = mScaleMin
            }
            currentPos <= 1.0f -> {
                val scale = (mScaleMax - mScaleMin) * Math.abs(1.0f - Math.abs(currentPos))
                val translationX = currentPos * -reduceX
                when {
                    currentPos <= -0.5 -> //两个view中间的临界，这时两个view在同一层，左侧View需要往X轴正方向移动覆盖的值()
                        page.translationX = translationX + mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f
                    currentPos <= 0.0f -> page.translationX = translationX
                    currentPos >= 0.5 -> //两个view中间的临界，这时两个view在同一层
                        page.translationX = translationX - mCoverWidth * Math.abs(Math.abs(currentPos) - 0.5f) / 0.5f
                    else -> page.translationX = translationX
                }
                page.scaleX = scale + mScaleMin
                page.scaleY = scale + mScaleMin
            }
            else -> {
                page.scaleX = mScaleMin
                page.scaleY = mScaleMin
                page.translationX = -reduceX - mCoverWidth
            }
        }
    }
}