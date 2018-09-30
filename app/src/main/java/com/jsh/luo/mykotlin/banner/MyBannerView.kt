package com.jsh.luo.mykotlin.banner

import android.content.Context
import android.os.Handler
import android.support.annotation.AttrRes
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.jsh.luo.mykotlin.R
import com.jsh.luo.mykotlin.Utils
import com.jsh.luo.mykotlin.banner.holder.MyHolderCreate
import kotlinx.android.synthetic.main.banner_view.view.*
import java.lang.IllegalArgumentException
import java.lang.reflect.Field

class MyBannerView<T>(context: Context,attrs: AttributeSet?,@AttrRes defStyleAttr : Int) : RelativeLayout(context,attrs,defStyleAttr){

    private var mModePadding : Int = 0
    private var mIndicatorAlign : Int = 1
    private var mIndicatorPaddingTop : Int = 0
    private var mIndicatorPaddingBottom : Int = 0

    private var mCurrentItem = 0

    private var mIsAutoPlay: Boolean = false

    private val mHandler = Handler()
    private var mAdapter :MyPagerAdapter<T>? = null
    private var mData: List<T>? = null
    private var mViewPagerScroller: ViewPagerScroller? = null//控制ViewPager滑动速度的Scroller

    private val mLooperRunnable = Runnable {
        if(mIsAutoPlay){
            mCurrentItem = view_pager.currentItem
            mCurrentItem ++
            if(mCurrentItem == mAdapter?.count!! - 1){

            }
        }
    }

    constructor(context: Context) : this(context,null)

    constructor(context: Context,attrs: AttributeSet?) : this(context,attrs,0)

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.banner_view,this,true)
        view_pager.offscreenPageLimit = 4

        mModePadding = Utils.dp2px(30).toInt()
        // 初始化Scroller
        initViewPagerScroll()
        sureIndicatorPosition()
    }

    /**
     * 设置ViewPager的滑动速度
     **/
    private fun initViewPagerScroll(){
        try {
            val mScroller: Field? = ViewPager::class.java.getDeclaredField("mScroller")
            mScroller?.isAccessible = true
            mViewPagerScroller = ViewPagerScroller(view_pager.context)
            mScroller?.set(view_pager, mViewPagerScroller)
        }catch (e : NoSuchFieldException){
            e.printStackTrace()
        }catch (e : IllegalAccessException){
            e.printStackTrace()
        }catch (e : IllegalArgumentException){
            e.printStackTrace()
        }
    }

    private fun sureIndicatorPosition(){
        when (mIndicatorAlign) {
            IndicatorAlign.LEFT.ordinal -> setIndicatorAlign(IndicatorAlign.LEFT)
            IndicatorAlign.CENTER.ordinal -> setIndicatorAlign(IndicatorAlign.CENTER)
            else -> setIndicatorAlign(IndicatorAlign.RIGHT)
        }
    }

    /**
     * 设置Indicator 的对齐方式
     * @param indicatorAlign {@link IndicatorAlign#CENTER }{@link IndicatorAlign#LEFT }{@link IndicatorAlign#RIGHT }
     */
    fun setIndicatorAlign(indicatorAlign: IndicatorAlign){
        mIndicatorAlign = indicatorAlign.ordinal
        val mLayoutParams : LayoutParams = banner_indicator_container.layoutParams as LayoutParams
        when(indicatorAlign){
            IndicatorAlign.LEFT -> mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            IndicatorAlign.RIGHT -> mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            IndicatorAlign.CENTER -> mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }
        mLayoutParams.setMargins(0,mIndicatorPaddingTop,0,mIndicatorPaddingBottom)
        banner_indicator_container.layoutParams = mLayoutParams
    }

    fun setPages(data : List<T>? ,myHolderCreate: MyHolderCreate<*>?){
        if(data == null || myHolderCreate == null){
            return
        }

        this.mData = data
        //如果在播放，就先让播放停止
        pause()
    }

    fun pause(){
        mIsAutoPlay = false
        m
    }
}