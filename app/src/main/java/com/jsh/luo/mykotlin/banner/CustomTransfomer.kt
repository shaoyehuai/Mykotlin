package com.jsh.luo.mykotlin.banner

import android.support.v4.view.ViewPager
import android.view.View

class CustomTransfomer : ViewPager.PageTransformer{

    companion object {
        private val MIN_SCALE = 0.9F
    }

    override fun transformPage(page: View, position: Float) {
        if (position >= -1 && position <= 1){
            page.scaleY = Math.max(MIN_SCALE,Math.abs(position))
        }else{
            page.scaleY = MIN_SCALE
        }
    }
}