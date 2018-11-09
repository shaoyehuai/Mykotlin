package com.jsh.luo.mykotlin.view.banner

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.support.annotation.AttrRes
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.jsh.luo.mykotlin.R
import com.jsh.luo.mykotlin.Utils
import kotlinx.android.synthetic.main.mz_banner_effect_layout.view.*
import java.lang.IllegalArgumentException
import java.lang.reflect.Field
import android.support.annotation.DrawableRes
import android.widget.LinearLayout
import com.jsh.luo.mykotlin.view.banner.holder.MyViewHolder
import com.jsh.luo.mykotlin.view.banner.transformer.CoverModeTransformer
import com.jsh.luo.mykotlin.view.banner.transformer.ScaleYTransformer

/**
 * create by ljs on 2018-11-08
 * */
class MyBannerView<T> : RelativeLayout{

    private var mModePadding : Int = 0
    private var mIndicatorAlign : Int = 1
    private var mIndicatorPaddingTop : Int = 0
    private var mIndicatorPaddingBottom : Int = 0
    private var mIndicatorPaddingLeft : Int = 0
    private var mIndicatorPaddingRight : Int = 0
    private var mMZModePadding = 0//在仿魅族模式下，由于前后显示了上下一个页面的部分，因此需要计算这部分padding

    private val mIndicatorRes = intArrayOf(R.drawable.indicator_normal, R.drawable.indicator_selected)

    private var mCurrentItem = 0
    private var mDelayedTime = 0 //banner展示时间

    private var mIsAutoPlay: Boolean = false
    private var mIsCanLoop : Boolean = false //是否可以轮播
    private var mIsOpenMZEffect = true //开启魅族Banner效果
    private var mIsMiddlePageCover = true //中间Page是否覆盖两边，默认覆盖

    private val mHandler = Handler()
    private var mAdapter :MyPagerAdapter<T>? = null
    private var mData: List<T>? = null
    private val mIndicators : ArrayList<ImageView>  = ArrayList()
    private var mViewPagerScroller: ViewPagerScroller? = null//控制ViewPager滑动速度的Scroller

    private var mOnPageChangeListener : ViewPager.OnPageChangeListener? = null
    private var mBannerPageClickListener: BannerPageClickListener? = null

    private val mLooperRunnable = object : Runnable {
        override fun run() {
            if (mIsAutoPlay) {
                mCurrentItem = view_pager.currentItem
                mCurrentItem ++
                if (mCurrentItem == mAdapter?.count!! - 1) {
                    mCurrentItem = 0
                    view_pager.setCurrentItem(mCurrentItem, false)
                    mHandler.postDelayed(this, mDelayedTime.toLong())
                } else {
                    view_pager.currentItem = mCurrentItem
                    mHandler.postDelayed(this, mDelayedTime.toLong())
                }
            } else {
                mHandler.postDelayed(this, mDelayedTime.toLong())
            }
        }
    }

    constructor(context: Context) : this(context,null)

    constructor(context: Context,attrs: AttributeSet?) : this(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet?,@AttrRes defStyleAttr : Int) : super(context, attrs, defStyleAttr){
        readAttrs(context,attrs)
        init()
    }

    private fun readAttrs(context : Context ,attrs : AttributeSet?){
        val typedArray : TypedArray = context.obtainStyledAttributes(attrs,R.styleable.MyBannerView)
        mIsOpenMZEffect = typedArray.getBoolean(R.styleable.MyBannerView_open_mz_mode,true)
        mIsMiddlePageCover = typedArray.getBoolean(R.styleable.MyBannerView_middle_page_cover,true)
        mIsCanLoop = typedArray.getBoolean(R.styleable.MyBannerView_canLoop,true)
        mIndicatorAlign = typedArray.getInt(R.styleable.MyBannerView_indicatorAlign,IndicatorAlign.CENTER.ordinal)
        mIndicatorPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MyBannerView_indicatorPaddingLeft,0)
        mIndicatorPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MyBannerView_indicatorPaddingRight,0)
        mIndicatorPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MyBannerView_indicatorPaddingTop,0)
        mIndicatorPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MyBannerView_indicatorPaddingBottom,0)
        typedArray.recycle()
    }

    private fun init(){
        if(mIsOpenMZEffect){
            LayoutInflater.from(context).inflate(R.layout.mz_banner_effect_layout,this,true)
        }else{
            LayoutInflater.from(context).inflate(R.layout.mz_banner_nomal_layout,this,true)
        }
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
     * 是否开启魅族模式
     */
    private fun setOpenMZEffect() {
        // 魅族模式
        if (mIsOpenMZEffect) {
            if (mIsMiddlePageCover) {
                // 中间页面覆盖两边，和魅族APP 的banner 效果一样。
                view_pager.setPageTransformer(true, CoverModeTransformer(view_pager))
            } else {
                // 中间页面不覆盖，页面并排，只是Y轴缩小
                view_pager.setPageTransformer(false, ScaleYTransformer())
            }
        }
    }

    /**
     * 初始化指示器Indicator
     */
    private fun initIndicator(){
        banner_indicator_container.removeAllViews()
        mIndicators.clear()
        for (i in 0 until mData!!.size){
            val imageView = ImageView(context)
            if(mIndicatorAlign == IndicatorAlign.LEFT.ordinal){
                if(i == 0){
                    val paddingLeft = if (mIsOpenMZEffect) mIndicatorPaddingLeft + mMZModePadding else mIndicatorPaddingLeft
                    imageView.setPadding(paddingLeft + 6,0,6,0)
                } else{
                    imageView.setPadding(6,0,6,0)
                }
            }else if(mIndicatorAlign == IndicatorAlign.RIGHT.ordinal){
                if(i == mData!!.size - 1){
                    val paddingRight = if(mIsOpenMZEffect) mMZModePadding + mIndicatorPaddingRight else mIndicatorPaddingRight
                    imageView.setPadding(6,0,6 + paddingRight,0)
                }else{
                    imageView.setPadding(6,0,6,0)
                }
            }else{
                imageView.setPadding(6,0,6,0)
            }

            if(i == (mCurrentItem % mData!!.size)){
                imageView.setImageResource(mIndicatorRes[1])
            }else{
                imageView.setImageResource(mIndicatorRes[0])
            }

            mIndicators.add(imageView)
            banner_indicator_container.addView(imageView)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(!mIsCanLoop) {
            return super.dispatchTouchEvent(ev)
        }

        when (ev!!.action) {
            // 按住Banner的时候，停止自动轮播
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_DOWN -> {
                val paddingLeft = view_pager.left
                val touchX = ev.rawX
                // 如果是魅族模式，去除两边的区域
                if (touchX >= paddingLeft && touchX < Utils.getScreenWidth(context) - paddingLeft) {
                    pause()
                }
            }
            MotionEvent.ACTION_UP -> start()
        }
        return super.dispatchTouchEvent(ev)
    }

    /******************************************************************************************************/
    /**                             对外API                                                               **/
    /******************************************************************************************************/
    /**
     * 开始轮播
     *
     * 应该确保在调用用了[{][.setPages] 之后调用这个方法开始轮播
     */
    fun start() {
        // 如果Adapter为null, 说明还没有设置数据，这个时候不应该轮播Banner
        if (mAdapter == null) {
            return
        }
        if (mIsCanLoop) {
            pause()
            mIsAutoPlay = true
            mHandler.postDelayed(mLooperRunnable, mDelayedTime.toLong())
        }
    }

    //外部可以访问的方法
    fun pause(){
        mIsAutoPlay = false
        mHandler.removeCallbacks(mLooperRunnable)
    }

    /**
     * 设置是否可以轮播
     * @param canLoop
     */
    fun setCanLoop(canLoop : Boolean) {
        this.mIsCanLoop = canLoop

        if(!canLoop){
            pause()
        }
    }

    /*设置展示时间.*/
    fun setDelayedTime(delayed: Int) {
        this.mDelayedTime = delayed
    }

    fun addPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener){
        this.mOnPageChangeListener = onPageChangeListener
    }

    /**
     *  添加Page点击事件
     * @param bannerPageClickListener {@link BannerPageClickListener}
     */
    fun setBannerPageClickListener(bannerPageClickListener: BannerPageClickListener){
        this.mBannerPageClickListener = bannerPageClickListener
    }

    /**
     * 是否显示Indicator
     * @param visible true 显示Indicator，否则不显示
     */
    fun setIndicatorVisible(visible : Boolean){
        if(visible){
            banner_indicator_container.visibility = View.VISIBLE
        }else{
            banner_indicator_container.visibility = View.GONE
        }
    }

    /**
     * set indicator padding
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    fun setIndicatorPadding(paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int) {
        mIndicatorPaddingLeft = paddingLeft
        mIndicatorPaddingTop = paddingTop
        mIndicatorPaddingRight = paddingRight
        mIndicatorPaddingBottom = paddingBottom
        sureIndicatorPosition()
    }

    /**
     * 返回ViewPager
     * @return [ViewPager]
     */
    fun getViewPager(): ViewPager = view_pager

    /**
     * 设置indicator 图片资源
     * @param unSelectRes  未选中状态资源图片
     * @param selectRes  选中状态资源图片
     */
    fun setIndicatorRes(@DrawableRes unSelectRes: Int, @DrawableRes selectRes: Int) {
        mIndicatorRes[0] = unSelectRes
        mIndicatorRes[1] = selectRes
    }

    /**
     * 设置数据，这是最重要的一个方法。
     * <p>其他的配置应该在这个方法之前调用</p>
     * @param data Banner 展示的数据集合
     * @param myHolder  ViewHolder生成器 {@link MZHolderCreator} And {@link MZViewHolder}
     */
    fun setPages(data: List<T>?, myHolder : MyViewHolder<T>?){
        if(data == null || myHolder == null){
            return
        }

        this.mData = data
        //如果在播放，就先让播放停止
        pause()

        //增加一个逻辑：由于魅族模式会在一个页面展示前后页面的部分，因此，数据集合的长度至少为3,否则，自动为普通Banner模式
        //不管配置的:open_mz_mode 属性的值是否为true

        if(data.size < 3){
            mIsOpenMZEffect = false
            val layoutParams : MarginLayoutParams= view_pager.layoutParams as MarginLayoutParams
            layoutParams.setMargins(0,0,0,0)
            view_pager.layoutParams = layoutParams
            clipChildren = true
            view_pager.clipChildren = true
        }

        setOpenMZEffect()
        //初始化Indicator
        initIndicator()

        mAdapter = MyPagerAdapter(data,myHolder,mIsCanLoop)
        mAdapter!!.setupViewPager(view_pager)
        mAdapter!!.setPageClickListener(mBannerPageClickListener)

        view_pager.clearOnPageChangeListeners()

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val realPosition = position % mIndicators.size
                mOnPageChangeListener?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mCurrentItem = position

                // 切换indicator
                val realSelectPosition = mCurrentItem % mIndicators.size
                for (i in 0 until mData!!.size) {
                    if (i == realSelectPosition) {
                        mIndicators[i].setImageResource(mIndicatorRes[1])
                    } else {
                        mIndicators[i].setImageResource(mIndicatorRes[0])
                    }
                }
                // 不能直接将mOnPageChangeListener 设置给ViewPager ,否则拿到的position 是原始的position
                mOnPageChangeListener?.onPageSelected(realSelectPosition)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> mIsAutoPlay = false
                    ViewPager.SCROLL_STATE_SETTLING -> mIsAutoPlay = true
                }
                mOnPageChangeListener?.onPageScrollStateChanged(state)
            }
        })
    }

    /**
     * 设置Indicator 的对齐方式
     * @param indicatorAlign {@link IndicatorAlign#CENTER }{@link IndicatorAlign#LEFT }{@link IndicatorAlign#RIGHT }
     */
    fun setIndicatorAlign(indicatorAlign: IndicatorAlign){
        mIndicatorAlign = indicatorAlign.ordinal
        val layoutParams = banner_indicator_container.layoutParams as RelativeLayout.LayoutParams
        when {
            indicatorAlign === IndicatorAlign.LEFT -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            indicatorAlign === IndicatorAlign.RIGHT -> layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            else -> layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        }

        layoutParams.setMargins(0, mIndicatorPaddingTop, 0, mIndicatorPaddingBottom)
        banner_indicator_container.layoutParams = layoutParams
    }

    fun getIndicatorContainer(): LinearLayout {
        return banner_indicator_container
    }

    /**
     * 设置ViewPager切换的速度
     * @param duration 切换动画时间
     */
    fun setDuration(duration: Int) {
        mViewPagerScroller?.duration = duration
    }

    /**
     * 设置是否使用ViewPager默认是的切换速度
     * @param useDefaultDuration 切换动画时间
     */
    fun setUseDefaultDuration(useDefaultDuration: Boolean) {
        mViewPagerScroller?.setUseDefaultDuration(useDefaultDuration)
    }

    /**
     * 获取Banner页面切换动画时间
     * @return
     */
    fun getDuration(): Int {
        if(mViewPagerScroller == null){
            return 800
        }
        return mViewPagerScroller!!.getScrollDuration()
    }
}