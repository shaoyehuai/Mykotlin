package com.jsh.luo.mykotlin.banner

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 *
 *＊由于ViewPager 默认的切换速度有点快，因此用一个Scroller 来控制切换的速度
 * <p>而实际上ViewPager 切换本来就是用的Scroller来做的，因此我们可以通过反射来</p>
 * <p>获取取到ViewPager 的 mScroller 属性，然后替换成我们自己的Scroller</p>
 */
class ViewPagerScroller(context: Context?, interpolator: Interpolator?, flywheel: Boolean) : Scroller(context, interpolator, flywheel) {

    var mDuration = 800// ViewPager默认的最大Duration为600,我们默认稍微大一点。值越大越慢。
    var mIsUseDefaultDuration = false

    constructor(context: Context?) : this(context,null)

    constructor(context: Context?,interpolator: Interpolator?) : this(context,interpolator,true)

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy,mDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, if (mIsUseDefaultDuration) duration else mDuration)
    }
}