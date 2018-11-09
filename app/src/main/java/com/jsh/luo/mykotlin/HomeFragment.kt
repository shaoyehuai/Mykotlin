package com.jsh.luo.mykotlin

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jsh.luo.mykotlin.view.banner.holder.MyViewHolder
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.jsh.luo.mykotlin.view.banner.BannerPageClickListener
import com.jsh.luo.mykotlin.view.banner.MyBannerView
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(){

    private var mTitle : String? = null
    val TAG = "HomeFragment"
    private val RES = intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher)
    private val BANNER = intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher)

    private var mBanner : MyBannerView<Int>? = null

    companion object {
        fun getInstance(title : String) : HomeFragment {
            val fragment = HomeFragment()
            fragment.mTitle = title
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_home,container,false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        mBanner = view.findViewById(R.id.banner)

        mBanner?.setBannerPageClickListener(object : BannerPageClickListener {
            override fun onPageClick(view: View, position: Int) {
                Toast.makeText(context, "click page:$position", Toast.LENGTH_LONG).show()
            }
        })

        mBanner?.addPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.e(TAG, "----->addPageChangeListener:" + position + "positionOffset:" + positionOffset + "positionOffsetPixels:" + positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                Log.e(TAG, "addPageChangeListener:$position")
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        val list = arrayListOf<Int>()
        for (i in 0 until RES.size) {
            list.add(RES[i])
        }

        val bannerList = arrayListOf<Int>()
        for (i in 0 until BANNER.size) {
            bannerList.add(BANNER[i])
        }
        banner.setIndicatorVisible(true)
        // 代码中更改indicator 的位置
        //mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
        //mMZBanner.setIndicatorPadding(10,0,0,150);
        mBanner?.setPages(bannerList, BannerViewHolder())
    }

    override fun onPause() {
        super.onPause()
        mBanner?.pause()
    }

    override fun onResume() {
        super.onResume()
        mBanner?.start()
    }

    class BannerViewHolder : MyViewHolder<Int>{

        private var mImageView: ImageView? = null

        @SuppressLint("InflateParams")
        override fun createView(context: Context): View {
            // 返回页面布局文件
            val view = LayoutInflater.from(context).inflate(R.layout.banner_item, null)
            mImageView = view.findViewById(R.id.banner_image) as ImageView
            return view
        }

        override fun onBind(context: Context, position: Int, data: Int) {
            mImageView?.setImageResource(data)
        }
    }
}
