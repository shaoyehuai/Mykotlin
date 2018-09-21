package com.jsh.luo.mykotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by luo on 2018/9/18.
 */
class MainActivity : AppCompatActivity(){

    private var mIndex : Int = 0

    private val mTabTitle = arrayOf("首页","推荐","我的")

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment : Fragment? = null
    private var mDiscoveryFragment : Fragment? = null
    private var mHotFragment : Fragment? = null
    private var mMineFragment : Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        SystemBarUtils.setFitsSystemWindows(this,true)
        SystemBarUtils().addStatusViewWithColor(this,resources.getColor(R.color.colorPrimary))

        if(savedInstanceState != null){
            mIndex = savedInstanceState.getInt("mCurrentIndex")
        }

        (0 until mTabTitle.size).mapTo(mTabEntities){TabEntity(mTabTitle[it],R.mipmap.ic_launcher,R.mipmap.ic_launcher)}
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener{
            override fun onTabSelect(position: Int) {

            }

            override fun onTabReselect(position: Int) {

            }
        })

        tab_layout.currentTab = mIndex

        switchFragment(mIndex)
    }

    private fun switchFragment(index : Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)

        when(index){
            0
            -> mHomeFragment?.also {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTabTitle[index]).also {
                mHomeFragment = it
                transaction.add(R.id.fl_content, it, "home")
            }
        }
    }

    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.also { transaction.hide(it) }
        mDiscoveryFragment?.also { transaction.hide(it) }
        mHotFragment?.also { transaction.hide(it) }
        mMineFragment?.also { transaction.hide(it) }
    }
}