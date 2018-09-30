package com.jsh.luo.mykotlin.banner

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.jsh.luo.mykotlin.banner.holder.MyHolderCreate
import com.jsh.luo.mykotlin.banner.holder.MyViewHolder
import java.lang.RuntimeException

class MyPagerAdapter<T>(data : List<T>,myHolderCreate: MyHolderCreate<*>,canLoop : Boolean) : PagerAdapter(){

    var mPageClickListener : BannerPageClickListener? = null

    private var mViewPager : ViewPager? = null
    private var mData : MutableList<T>? = null
    private var canLoop : Boolean = false
    private val mLooperCountFactor = 500
    private var myHolderCreate : MyHolderCreate<*>? = null

    init {
        this.canLoop = canLoop
        this.myHolderCreate = myHolderCreate
        if(mData == null) mData = ArrayList()

        mData?.addAll(data)
    }

    override fun getCount(): Int = if (canLoop) getRealCount() * mLooperCountFactor else getRealCount()

    override fun isViewFromObject(view: View, `object`: Any): Boolean = `object` === view

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getView(position,container)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    fun setupViewPager(viewPager: ViewPager){
        this.mViewPager = viewPager
        mViewPager?.adapter = this
        mViewPager?.let { it.adapter?.notifyDataSetChanged() }
        mViewPager?.currentItem = if(canLoop) getStartSelectItem() else 0
    }

    private fun getStartSelectItem() : Int{
        if(getRealCount() == 0){
            return 0
        }

        // 我们设置当前选中的位置为Integer.MAX_VALUE / 2,这样开始就能往左滑动
        // 但是要保证这个值与getRealPosition 的 余数为0，因为要从第一页开始显示
        val realCount = getRealCount()
        var currentItem = realCount * mLooperCountFactor * 2
        if(currentItem % getRealCount() == 0){
            return currentItem
        }

        while (currentItem % getRealCount() != 0){
            currentItem ++
        }
        return currentItem
    }

    private fun getRealCount() : Int = if(mData == null) 0 else mData!!.size

    private fun getView(position : Int ,container: ViewGroup) : View{
        val realPosition : Int = position % getRealCount()
        val holder : MyViewHolder<T> = myHolderCreate?.createViewHolder() as MyViewHolder<T>? ?: throw RuntimeException("can not return a null holder")

        val view :View =holder.createView(container.context)
        if(mData != null && mData!!.size > 0) {
            holder.onBind(container.context,realPosition,mData?.get(realPosition)!!)
        }
        view.setOnClickListener { v: View ->
            if(mPageClickListener != null){
                mPageClickListener?.onPageClick(v,realPosition)
            }
        }
        return view
    }
}
