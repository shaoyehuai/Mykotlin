package com.jsh.luo.mykotlin.view.banner.transformer

import android.support.v4.view.ViewPager
import android.view.View



class ScaleYTransformer : ViewPager.PageTransformer {

    companion object {
        private const val MIN_SCALE = 0.9f
    }

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> page.scaleY = MIN_SCALE
            position <= 1 -> {
                //
                val scale = Math.max(MIN_SCALE, 1 - Math.abs(position))
                page.scaleY = scale
                /*page.setScaleX(scale);
            if(position<0){
                page.setTranslationX(width * (1 - scale) /2);
            }else{
                page.setTranslationX(-width * (1 - scale) /2);
            }*/

            }
            else -> page.scaleY = MIN_SCALE
        }
    }
}
